/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.scheduler;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.IEjecucionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.EjecucionProcesoDTO;
import co.com.claro.model.dto.request.LoadPlanRequestDTO;
import co.com.claro.model.dto.request.LoadPlanStartupParameterRequestDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.EjecucionProceso;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.WsTransformacion;
import co.com.claro.service.rest.Constantes;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.util.Crypto;
import co.com.claro.service.rest.util.ResponseWrapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.oracle.xmlns.odi.odiinvoke.FacadeODI;
import com.oracle.xmlns.odi.odiinvoke.LoadPlanStatusType;
import com.oracle.xmlns.odi.odiinvoke.OdiStartLoadPlanType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.HEAD;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author ronal
 */
@Stateless

public class EjecucionProgramacion implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            System.out.println("Ejecutando job");
            JobDetail _jobDetail = jec.getJobDetail();
            WsTransformacion wsTransformacion = (WsTransformacion) _jobDetail.getJobDataMap().get("wsTransformacion");

            System.out.println(("Paquete: " + wsTransformacion.getPaqueteWs()));

            IEjecucionDAO logEjecucionDAO = (IEjecucionDAO) _jobDetail.getJobDataMap().get("logEjecucionDAO");
            Conciliacion conciliacion = (Conciliacion) _jobDetail.getJobDataMap().get("conciliacion");
            ConciliacionDAO conciliacionDAO = (ConciliacionDAO) _jobDetail.getJobDataMap().get("conciliacionDAO");
            ParametroDAO parametroDAO = (ParametroDAO) _jobDetail.getJobDataMap().get("parametroDAO");
            LogAuditoriaDAO logAuditoriaDAO = (LogAuditoriaDAO) _jobDetail.getJobDataMap().get("logAuditoriaDAO");
            //conciliacionDAO.find()
            //Conciliacion conciliacion = wsTransformacion.getConciliacion();
            System.out.println("Conciliacion:" + conciliacion.getNombre());

            // 1. Registrar log de eventos para inicio de integración
            Conciliacion entidadPadre = conciliacionDAO.find(conciliacion.getId());
            EjecucionProceso logAud = new EjecucionProceso();
            logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            logAud.setConciliacion(null);
            logAud.setEstadoEjecucion("INICIADA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            logAud.setFechaEjecucion(new Date());
            logAud.setNombre("AGENDADA:" + conciliacion.getNombre());
            logAud.setNombreConciliacion(conciliacion.getNombre());

            System.out.println("OBJ" + logAud.toString());
            logEjecucionDAO.create(logAud);
            logAud.setConciliacion(entidadPadre);
            logEjecucionDAO.edit(logAud);
            entidadPadre.addEjecucionProceso(logAud);
            conciliacionDAO.edit(entidadPadre);

            // 2. Lanzar odi
            String wsdlLocation;
            try {
                wsdlLocation = parametroDAO.findByParametro("SISTEMA", "V_odiUrl");
            } catch (Exception e) {
                wsdlLocation = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
            }
            String odiUsuario;
            try {
                odiUsuario = Crypto.cryptoParam(parametroDAO.findByParametro("SEGURIDAD", "V_odiUsuario"));
            } catch (Exception e) {
                odiUsuario = "EQK7054A";
            }
            String odiPassword;
            try {
                odiPassword = Crypto.cryptoParam(parametroDAO.findByParametro("SEGURIDAD", "V_odiPassword"));
            } catch (Exception e) {
                odiPassword = "1234567";
            }
            String odiWorkRepository;
            try {
                odiWorkRepository = Crypto.cryptoParam(parametroDAO.findByParametro("SEGURIDAD", "V_odiWorkRepository"));
            } catch (Exception e) {
                odiWorkRepository = "WRDEV_ASSURANCE1";
            }
            String odiContext;
            try {
                odiContext = Crypto.cryptoParam(parametroDAO.findByParametro("SEGURIDAD", "V_odiContext"));
            } catch (Exception e) {
                odiContext = "CNTX_DESARROLLO";
            }

            // 2.2 Consultar última ejecución en log
            ConciliacionDTO cdto_ = entidadPadre.toDTO();
            EjecucionProcesoDTO ejecucion = null;
            if (!cdto_.getEjecucionesProceso().isEmpty()) {
                ejecucion = cdto_.getEjecucionesProceso().iterator().next();
            }

            //String paquete = conciliacion.getTransformaciones().iterator().next().getPaqueteWs();
            FacadeODI fachadaOdi = new FacadeODI();
            // 2.3 Lanzar odi s i no hay última ejecución si su id plan es cero
            if (ejecucion != null && ejecucion.getIdPlanInstance() != null && ejecucion.getIdPlanInstance() != "0") {
                try {
                    List<LoadPlanRequestDTO> lstLoadRequest = new ArrayList<LoadPlanRequestDTO>();
                    LoadPlanRequestDTO loadrequest = new LoadPlanRequestDTO();
                    long idPlan = Long.parseLong(ejecucion.getIdPlanInstance());
                    loadrequest.setLoadPlanInstanceId(idPlan);
                    loadrequest.setLoadPlanRunNumber(1);
                    lstLoadRequest.add(loadrequest);
                    List<LoadPlanStatusType> responses = fachadaOdi.loadPlanStatus(wsdlLocation, odiUsuario, odiPassword, odiWorkRepository, lstLoadRequest);

                    String estadosNoEjecucion = parametroDAO.findByParametro("SISTEMA", "V_odiEstadosNoEjecucion");
                    String[] estados = estadosNoEjecucion.split(",");

                    if (responses.size() > 0 && Arrays.asList(estados).contains(responses.get(0).getLoadPlanStatus()) /*(responses.get(0).getLoadPlanStatus() == "R"
                            || responses.get(0).getLoadPlanStatus() == "Q"
                            || responses.get(0).getLoadPlanStatus() == "W"
                            || responses.get(0).getLoadPlanStatus() == "E")*/) {
                        // Ya está corriendo por tanto no puede volver a lanzarlo

                        throw new Exception("No es posible ejecutar el paquete " + wsTransformacion.getPaqueteWs() + " dado que esta en estado " + responses.get(0).getLoadPlanStatus());
                    }

                } catch (Exception e) {
                    throw e;
                }
            }

            List<LoadPlanStartupParameterRequestDTO> params = new ArrayList<LoadPlanStartupParameterRequestDTO>();
            LoadPlanStartupParameterRequestDTO param = new LoadPlanStartupParameterRequestDTO();
            param.setNombre("GLOBAL.V_CTL_PAQUETE");
            param.setValor(wsTransformacion.getPaqueteWs());
            params.add(param);
            LoadPlanStartupParameterRequestDTO param1 = new LoadPlanStartupParameterRequestDTO();
            param1.setNombre("GLOBAL.V_CTL_SESION");
            param1.setValor(parametroDAO.findByParametro("SISTEMA", "V_odiGLOBAL.V_CTL_SESION"));
            params.add(param1);

            System.out.println("wsdlLocation:" + wsdlLocation);
            try {
                // FacadeODI fachadaOdi = new FacadeODI();
                OdiStartLoadPlanType response = fachadaOdi.startLoadPlan(wsdlLocation, odiUsuario, odiPassword, odiWorkRepository, parametroDAO.findByParametro("SISTEMA", "V_odiGLOBAL.V_CTL_PAQUETE"), odiContext, params);
                System.out.println("después de starloadplan:**" + response.toString());

                // 3. poner log
                // Si todo va en orden registrar grabación con éxito
                Conciliacion _entidadPadre = conciliacionDAO.find(conciliacion.getId());
                EjecucionProceso _logAud = new EjecucionProceso();
                _logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
                _logAud.setConciliacion(null);
                _logAud.setEstadoEjecucion("INTEGRADA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
                _logAud.setFechaEjecucionExitosa(new Date());
                _logAud.setNombreConciliacion(conciliacion.getNombre());

                XmlMapper xmlMapper = new XmlMapper();
                String xml = xmlMapper.writeValueAsString(response.getStartedRunInformation());

                _logAud.setRespuesta(xml);
                _logAud.setNombre("AGENDADA:" + conciliacion.getNombre());
                Long planInstanceId = response.getStartedRunInformation().getOdiLoadPlanInstanceId();
                _logAud.setIdPlanInstance(planInstanceId.toString());

                System.out.println("GRABANDO REGISTRO EXITOSO: " + _logAud.toString());
                System.out.println("ID PLAN: " + response.getStartedRunInformation().getOdiLoadPlanInstanceId());
                logEjecucionDAO.create(_logAud);
                _logAud.setConciliacion(_entidadPadre);
                System.out.println("ENTIDDAD PADRE CONC: "+ _entidadPadre.getNombre());
                logEjecucionDAO.edit(_logAud);
                _entidadPadre.addEjecucionProceso(_logAud);
                conciliacionDAO.edit(_entidadPadre);

                // 3.1 en la implementación actual desde el front auditan otra tabla --[TBL_GAI_LOG_AUDITORIA]                
               /* Conciliacion entidadPadreJPA;
                EjecucionProceso entidadJPA = new EjecucionProceso();// entidad.toEntity();
                entidadJPA.setNombre(entidadPadre.getNombre());
                entidadJPA.setIdPlanInstance(planInstanceId.toString());
                entidadJPA.setConciliacion(entidadPadre);

                entidadPadreJPA = conciliacionDAO.find(entidadPadre.getId());
                if (entidadPadreJPA != null) {
                    entidadJPA.setConciliacion(null);
                    logEjecucionDAO.create(entidadJPA);
                    entidadJPA.setConciliacion(entidadPadreJPA);
                    logEjecucionDAO.edit(entidadJPA);
                    entidadPadreJPA.addEjecucionProceso(entidadJPA);
                    conciliacionDAO.edit(entidadPadreJPA);
                }*/
                LogAuditoria logAud_ = new LogAuditoria("EJECUCIONPROCESO", Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), "SISTEMA", _logAud.toString());
              //  logAuditoriaDAO.create(logAud_);
            } catch (Exception ex) {
                Conciliacion _entidadPadre = conciliacionDAO.find(conciliacion.getId());
                EjecucionProceso _logAud = new EjecucionProceso();
                _logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
                _logAud.setConciliacion(null);
                _logAud.setNombre("AGENDADA:" + conciliacion.getNombre());
                _logAud.setEstadoEjecucion("FALLIDA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
                _logAud.setFechaEjecucion(new Date());
                _logAud.setNombreConciliacion(conciliacion.getNombre());
                _logAud.setRespuesta(ex.toString());

                System.out.println("INTEGRACIÓN FALLIDA: " + ex.toString());

                logEjecucionDAO.create(_logAud);
                _logAud.setConciliacion(_entidadPadre);
                logEjecucionDAO.edit(_logAud);
                _entidadPadre.addEjecucionProceso(_logAud);
                conciliacionDAO.edit(_entidadPadre);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}
