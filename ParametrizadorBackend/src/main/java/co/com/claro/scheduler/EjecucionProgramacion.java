/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.scheduler;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.IEjecucionDAO;
import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.model.dto.request.LoadPlanStartupParameterRequestDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.EjecucionProceso;
import co.com.claro.model.entity.WsTransformacion;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.oracle.xmlns.odi.odiinvoke.FacadeODI;
import com.oracle.xmlns.odi.odiinvoke.OdiStartLoadPlanType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
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
            //conciliacionDAO.find()
            //Conciliacion conciliacion = wsTransformacion.getConciliacion();
            System.out.println("Concliacion:" + conciliacion.getNombre());

            // TODO: INTEGRAR CON ODI
            // 1. Registrar log de eventos para inicio de integración
            Conciliacion entidadPadre = conciliacionDAO.find(conciliacion.getId());
            EjecucionProceso logAud = new EjecucionProceso();
            logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            logAud.setConciliacion(null);
            logAud.setEstadoEjecucion("INICIADA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            logAud.setFechaEjecucion(new Date());
            logAud.setNombre("AGENDADA:" + conciliacion.getNombre());
            // logAud.setIdPlanInstance(conciliacion.getId().toString());
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
                wsdlLocation = parametroDAO.findByParametro("SISTEMA", "odiUrl");
            } catch (Exception e) {
                wsdlLocation = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
            }
            String odiUsuario;
            try {
                odiUsuario = parametroDAO.findByParametro("SISTEMA", "odiUsuario");
            } catch (Exception e) {
                odiUsuario = "EQK7054A";
            }
            String odiPassword;
            try {
                odiPassword = parametroDAO.findByParametro("SISTEMA", "odiPassword");
            } catch (Exception e) {
                odiPassword = "1234567";
            }
            String odiWorkRepository;
            try {
                odiWorkRepository = parametroDAO.findByParametro("SISTEMA", "odiWorkRepository");
            } catch (Exception e) {
                odiWorkRepository = "WRDEV_ASSURANCE1";
            }
            String odiContext;
            try {
                odiContext = parametroDAO.findByParametro("SISTEMA", "odiContext");
            } catch (Exception e) {
                odiContext = "CNTX_DESARROLLO";
            }

            List<LoadPlanStartupParameterRequestDTO> params = new ArrayList<LoadPlanStartupParameterRequestDTO>();
            LoadPlanStartupParameterRequestDTO param = new LoadPlanStartupParameterRequestDTO();
            param.setNombre("GLOBAL.V_CTL_PAQUETE");
            param.setValor("JP_NO_EXISTE");
            params.add(param);
            LoadPlanStartupParameterRequestDTO param1 = new LoadPlanStartupParameterRequestDTO();
            param1.setNombre("GLOBAL.V_CTL_SESION");
            param1.setValor("0");
            params.add(param1);

            System.out.println("wsdlLocation:" + wsdlLocation);
            try {
                FacadeODI fachadaOdi = new FacadeODI();
                OdiStartLoadPlanType response = fachadaOdi.startLoadPlan(wsdlLocation, odiUsuario, odiPassword, odiWorkRepository, wsTransformacion.getPaqueteWs(), odiContext, params);
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
                logEjecucionDAO.edit(_logAud);
                 _entidadPadre.addEjecucionProceso(_logAud);
                 conciliacionDAO.edit(_entidadPadre);
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
