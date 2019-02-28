package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.IEjecucionDAO;
import co.com.claro.ejb.dao.IWsTransformacionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.EjecucionProcesoDTO;
import co.com.claro.model.dto.EscenarioDTO;
import co.com.claro.model.dto.WsTransformacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.dto.request.LoadPlanRequestDTO;
import co.com.claro.model.dto.request.LoadPlanStartupParameterRequestDTO;
import co.com.claro.model.dto.request.StartLoadPlanRequestDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.EjecucionProceso;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Politica;
import co.com.claro.model.entity.WsTransformacion;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.i18n.I18N;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;
import co.com.claro.service.rest.util.Crypto;
import co.com.claro.service.rest.util.ResponseWrapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.oracle.xmlns.odi.odiinvoke.FacadeODI;
import com.oracle.xmlns.odi.odiinvoke.LoadPlanStatusType;
import com.oracle.xmlns.odi.odiinvoke.OdiCredentialType;
import com.oracle.xmlns.odi.odiinvoke.OdiStartLoadPlanType;
import com.oracle.xmlns.odi.odiinvoke.OdiStopLoadPlanResponse;
import com.oracle.xmlns.odi.odiinvoke.OdiStopLoadPlanType;
import com.oracle.xmlns.odi.odiinvoke.StopLoadPlanRequestType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static java.util.Comparator.comparing;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Clase que maneja el API Rest de la entidad
 *
 * @author Andres Bedoya
 */
@Stateless
@Path("conciliaciones")
public class ConciliacionREST {

    @Transient
    private static final Logger logger = Logger.getLogger(ConciliacionREST.class.getSimpleName());

    private String modulo = "CONCILIACIONES";

    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;

    @EJB
    protected PoliticaDAO padreDAO;

    @EJB
    protected ConciliacionDAO managerDAO;

    @EJB
    protected EscenarioDAO escenarioDAO;

    @EJB
    protected IWsTransformacionDAO transformacionDAO;

    @EJB
    protected IEjecucionDAO logEjecucionDAO;

    @EJB
    protected ParametroDAO parametroDAO;

    /**
     * Obtiene las Conciliaciones Paginadas
     *
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id,
     * nombre, fechaCreacion)
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby,
            @QueryParam("name") String name) {

        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});
        List<Conciliacion> lst = managerDAO.findAll(name);
        List<PadreDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(ConciliacionDTO::getId)).collect(toList());
        //UtilListas.ordenarLista(lstDTO, orderby);
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una Conciliacion por id
     *
     * @param id Identificador de conciliacion
     * @return Una Conciliacion que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON + ";charset=utf-8"})
    public ConciliacionDTO getById(@PathParam("id") int id) {

        logger.log(Level.INFO, "id:{0}", id);
        Conciliacion entidad = managerDAO.find(id);
        return entidad.toDTO();
    }

    /**
     * Busca las conciliaciones por cualquier columna
     *
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Conciliacions que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> findByAnyColumn(@QueryParam("texto") String texto) {
        logger.log(Level.INFO, "texto:{0}", texto);
        List<Conciliacion> lst = managerDAO.findByAnyColumn(texto);
        List<PadreDTO> lstDTO = new ArrayList<>();
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    @GET
    @Path("/politica/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> getByPolitica(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        List<ConciliacionDTO> lstDTO;
        List<Conciliacion> lst;
        lst = managerDAO.findByPolitica(id);
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(ConciliacionDTO::getId)).collect(toList());
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    @GET
    @Path("/conciliacionesRequierenAprobacion")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> findConciliacionesRequierenAprobacion() {
        //logger.log(Level.INFO, "tipo:{0}codPadre:{1}", new Object[]{requiereaprobacion});
        String requiereaprobacion = "SI";
        List<Conciliacion> lst = managerDAO.findByRequiereAprobacion(requiereaprobacion);
        List<ConciliacionDTO> lstDTO = lst.stream().map(item -> item.toDTO()).sorted(comparing(ConciliacionDTO::getId)).collect(toList());
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    @GET
    @Path("/conciliacionesEjecutables")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> findConciliacionesEjecutables() {
        //logger.log(Level.INFO, "tipo:{0}codPadre:{1}", new Object[]{requiereaprobacion});

        List<Conciliacion> lst = managerDAO.findByEjecutables();
        List<ConciliacionDTO> lstDTO = lst.stream().map(item -> item.toDTO()).sorted(comparing(ConciliacionDTO::getId)).collect(toList());
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Crea una nueva politica
     *
     * @param dto Entidad que se va a agregar
     * @return el la dto recien creada
     */
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(ConciliacionDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);

        try {
            Politica entidadPadreJPA;
            Conciliacion entidadJPA = dto.toEntity();
            entidadPadreJPA = padreDAO.find(dto.getIdPolitica());
            transformacionDAO.verificarSiExistePaqueteWs(dto.getPaquete());
            if (entidadPadreJPA != null) {
                entidadJPA.setPolitica(null);
                managerDAO.create(entidadJPA);
                entidadJPA.setPolitica(entidadPadreJPA);
                entidadPadreJPA.addConciliaciones(entidadJPA);
                //transformacionDAO.edit(transformacion);
                if (dto.getPaquete() != null) {
                    System.out.println("4 =>");
                    //crearPaquete(dto, entidadJPA);
                    WsTransformacion transformacion = new WsTransformacion();
                    transformacion.setFechaCreacion(Date.from(Instant.now()));
                    transformacion.setFechaAgendamiento(new Date());
                    transformacion.setNombreWs(dto.getPaquete());
                    transformacion.setPaqueteWs(dto.getPaquete());
                    transformacion.setConciliacion(entidadJPA);
                    entidadJPA.addTransformacion(transformacion);
                }
                managerDAO.edit(entidadJPA);
                padreDAO.edit(entidadPadreJPA);
            }
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), dto.getUsername(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud);

            ResponseWrapper wraper = new ResponseWrapper(true, I18N.getMessage("conciliaciones.save", entidadJPA.getNombre()), entidadJPA.toDTO());
            return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            if (e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, e.getCause().getMessage(), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            } else {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, I18N.getMessage("general.readerror"), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            }

        }
    }

    public void crearPaquete(ConciliacionDTO dto, Conciliacion entidadJPA) {
        // Si ya existe un paquete, debe actualizarlo
        Collection<WsTransformacion> lista = entidadJPA.getTransformaciones();
        if (lista.isEmpty()){        
            WsTransformacion transformacion = new WsTransformacion();
            transformacion.setFechaCreacion(Date.from(Instant.now()));
            transformacion.setNombreWs(dto.getPaquete());
            transformacion.setPaqueteWs(dto.getPaquete());
            transformacion.setConciliacion(entidadJPA);
            entidadJPA.addTransformacion(transformacion);
        } else{
            WsTransformacion transformacion = lista.iterator().next();
            transformacion.setFechaActualizacion(Date.from(Instant.now()));
            transformacion.setPaqueteWs(dto.getPaquete());
            transformacion.setNombreWs(dto.getPaquete());
            transformacion.setConciliacion(entidadJPA);
        }
    }

    /**
     * Actualiza la entidadDTO por su Id
     *
     * @param entidadDTO conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(ConciliacionDTO entidadDTO) {
        logger.log(Level.INFO, "entidad:{0}", entidadDTO);

        try {
            Politica entidadPadreJPA = null;
            transformacionDAO.verificarSiExistePaqueteWs(entidadDTO.getPaquete());
            List<WsTransformacion> results = transformacionDAO.validPaqueteWs(entidadDTO.getPaquete());
            String paquete = (results != null && !results.isEmpty()) ? results.get(0).getPaqueteWs() : "";
            if (entidadDTO.getIdPolitica() != null) {
                entidadPadreJPA = padreDAO.find(entidadDTO.getIdPolitica());
                if (entidadPadreJPA == null) {
                    throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidadDTO.getIdPolitica());
                }
            }
            //Hallar La entidadDTO actual para actualizarla
            Conciliacion entidadJPA = managerDAO.find(entidadDTO.getId());
            Politica oldPolitica = entidadJPA.getPolitica();
            if (entidadJPA != null) {
                entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
                entidadJPA.setNombre(entidadDTO.getNombre() != null ? entidadDTO.getNombre() : entidadJPA.getNombre());
                entidadJPA.setTablaDestino(entidadDTO.getTablaDestino() != null ? entidadDTO.getTablaDestino() : entidadJPA.getTablaDestino());
                entidadJPA.setCamposTablaDestino(entidadDTO.getCamposTablaDestino() != null ? entidadDTO.getCamposTablaDestino() : entidadJPA.getCamposTablaDestino());
                entidadJPA.setDescripcion(entidadDTO.getDescripcion() != null ? entidadDTO.getDescripcion() : entidadJPA.getDescripcion());
                entidadJPA.setUsuarioAsignado(entidadDTO.getUsuarioAsignado() != null ? entidadDTO.getUsuarioAsignado() : entidadJPA.getUsuarioAsignado());
                entidadJPA.setRequiereAprobacion(entidadDTO.getRequiereAprobacion() != null ? entidadDTO.getRequiereAprobacion() : entidadJPA.getRequiereAprobacion());
                entidadJPA.setPolitica(entidadDTO.getIdPolitica() != null ? (entidadPadreJPA != null ? entidadPadreJPA : null) : entidadJPA.getPolitica());

                if (entidadDTO.getPaquete() != null) {
                    if (!paquete.equalsIgnoreCase(entidadDTO.getPaquete())) {
                        crearPaquete(entidadDTO, entidadJPA);
                    }
                }
                managerDAO.edit(entidadJPA);
                if ((entidadPadreJPA != null)) {
                    entidadPadreJPA.addConciliaciones(entidadJPA);
                    padreDAO.edit(entidadPadreJPA);
                }
                LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), entidadDTO.getUsername(), entidadJPA.toString());
                logAuditoriaDAO.create(logAud);

                ResponseWrapper wraper = new ResponseWrapper(true, I18N.getMessage("conciliaciones.update", entidadJPA.getNombre()), entidadDTO);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            }
            
            if(entidadPadreJPA != null){
                padreDAO.detach(oldPolitica);
            }

            ResponseWrapper wraper = new ResponseWrapper(false, I18N.getMessage("conciliaciones.notfound", entidadJPA.getNombre()));
            return Response.ok(wraper, MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            if (e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, e.getCause().getMessage(), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            } else {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, I18N.getMessage("general.readerror"), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            }

        }
    }

    /**
     * Borra una conciliacion por su Id
     *
     * @param id Identificador de la identidad
     * @return el resultado de la operacion
     */
    @DELETE
    @Path("{id}/{username}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id, @PathParam("username") String username) {
        try {
            Conciliacion entidadJPA = managerDAO.find(id);
            ConciliacionDTO dto = entidadJPA.toDTO();
            Politica entidadPadreJPA = null;
            if (entidadJPA.getPolitica() != null) {
                entidadPadreJPA = padreDAO.find(entidadJPA.getPolitica().getId());
                entidadPadreJPA.removeConciliaciones(entidadJPA);
            }
            
            // Quitar paquete asociado a la conciliación
            Collection<WsTransformacion> lista = entidadJPA.getTransformaciones();
            if (!lista.isEmpty()){        
                WsTransformacion transformacion = lista.iterator().next();
                entidadJPA.removeTransformacion(transformacion);
            }
            
            managerDAO.remove(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, dto.toString());
            logAuditoriaDAO.create(logAud);
            if (entidadPadreJPA != null) {
                padreDAO.edit(entidadPadreJPA);
            }
            ResponseWrapper wraper = new ResponseWrapper(true, I18N.getMessage("conciliaciones.delete"), entidadJPA.getNombre());
            return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            if (e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, e.getCause().getMessage(), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            } else {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, I18N.getMessage("general.readerror"), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            }
        }

    }

    @GET
    @Path("/count")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public int count() {
        return managerDAO.count();
    }

    @POST
    @Path("/progEjecucion")
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response setProgramacionEjecucion(WsTransformacionDTO dto) {
        try {
            WsTransformacion entidadJPA = transformacionDAO.find(dto.getId());
            entidadJPA.setFechaAgendamiento(dto.getFechaAgendamiento());
            logger.log(Level.INFO, "fecha agendamiento:{0}", entidadJPA);
            transformacionDAO.edit(entidadJPA);

            ResponseWrapper wraper = new ResponseWrapper(true, I18N.getMessage("programaciones.programar"), entidadJPA.toDTO());
            return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            if (e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, e.getCause().getMessage(), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            } else {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, I18N.getMessage("general.readerror"), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            }
        }

    }

    @POST
    @Path("/ejecutar")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response ejecutar(WsTransformacionDTO request) {
        // 1. Registrar log de eventos para inicio de integración
        Conciliacion entidadPadre = managerDAO.find(request.getIdConciliacion());
        EjecucionProceso logAud = new EjecucionProceso();
        logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
        logAud.setConciliacion(null);
        logAud.setEstadoEjecucion("INICIADA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
        logAud.setFechaEjecucion(new Date());
        logAud.setNombre("EJECUTADA:" + entidadPadre.getNombre());
        logAud.setNombreConciliacion(entidadPadre.getNombre());

        logEjecucionDAO.create(logAud);
        logAud.setConciliacion(entidadPadre);
        logEjecucionDAO.edit(logAud);
        entidadPadre.addEjecucionProceso(logAud);
        managerDAO.edit(entidadPadre);

        // 2.1 Traer parámetros
        String wsdlLocation;
        try {
            wsdlLocation = parametroDAO.findByParametro("SISTEMA", "V_odiUrl");
        } catch (Exception e) {
            wsdlLocation = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
        }

        String odiUsuario;
        try {
            String _odiUsuario = parametroDAO.findByParametro("SEGURIDAD", "V_odiUsuario");
            odiUsuario = Crypto.cryptoParam(_odiUsuario);
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

                    ResponseWrapper wraper = new ResponseWrapper(false, "No es posible ejecutar el paquete " + request.getPaqueteWs() + " dado que esta en estado " + responses.get(0).getLoadPlanStatus(), 500);
                    return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                }

            } catch (Exception e) {
                if (e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                    ResponseWrapper wraper = new ResponseWrapper(false, e.getCause().getMessage(), 500);
                    return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                } else {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                    ResponseWrapper wraper = new ResponseWrapper(false, e.getMessage(), 500);

                    return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                }
            }
        }

        List<LoadPlanStartupParameterRequestDTO> params = new ArrayList<LoadPlanStartupParameterRequestDTO>();
        LoadPlanStartupParameterRequestDTO param = new LoadPlanStartupParameterRequestDTO();
        param.setNombre("GLOBAL.V_CTL_PAQUETE");
        param.setValor(request.getPaqueteWs());
        params.add(param);
        LoadPlanStartupParameterRequestDTO param1 = new LoadPlanStartupParameterRequestDTO();
        param1.setNombre("GLOBAL.V_CTL_SESION");
        param1.setValor(parametroDAO.findByParametro("SISTEMA", "V_odiGLOBAL.V_CTL_SESION"));
        params.add(param1);

        System.out.println("wsdlLocation:" + wsdlLocation);
        try {
            OdiStartLoadPlanType response = fachadaOdi.startLoadPlan(wsdlLocation, odiUsuario, odiPassword, odiWorkRepository, parametroDAO.findByParametro("SISTEMA", "V_odiGLOBAL.V_CTL_PAQUETE"), odiContext, params);
            System.out.println("después de starloadplan:**" + response.toString());

            // 3. poner log
            // Si todo va en orden registrar grabación con éxito
            Conciliacion _entidadPadre = managerDAO.find(entidadPadre.getId());
            EjecucionProceso _logAud = new EjecucionProceso();
            _logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            _logAud.setConciliacion(null);
            _logAud.setEstadoEjecucion("INTEGRADA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            _logAud.setFechaEjecucionExitosa(new Date());
            _logAud.setNombreConciliacion(entidadPadre.getNombre());

            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(response.getStartedRunInformation());

            _logAud.setRespuesta(xml);
            _logAud.setNombre("EJECUTADA:" + entidadPadre.getNombre());
            Long planInstanceId = response.getStartedRunInformation().getOdiLoadPlanInstanceId();
            _logAud.setIdPlanInstance(planInstanceId.toString());

            logEjecucionDAO.create(_logAud);
            _logAud.setConciliacion(_entidadPadre);
            logEjecucionDAO.edit(_logAud);
            _entidadPadre.addEjecucionProceso(_logAud);
            managerDAO.edit(_entidadPadre);

            // 3.1 en la implementación actual desde el front auditan otra tabla --[TBL_GAI_LOG_AUDITORIA]                
            Conciliacion entidadPadreJPA;
            EjecucionProceso entidadJPA = new EjecucionProceso();// entidad.toEntity();
            entidadJPA.setNombre(entidadPadre.getNombre());
            entidadJPA.setIdPlanInstance(planInstanceId.toString());
            entidadJPA.setConciliacion(entidadPadre);

            entidadPadreJPA = managerDAO.find(entidadPadre.getId());
            if (entidadPadreJPA != null) {
                entidadJPA.setConciliacion(null);
                logEjecucionDAO.create(entidadJPA);
                entidadJPA.setConciliacion(entidadPadreJPA);
                logEjecucionDAO.edit(entidadJPA);
                entidadPadreJPA.addEjecucionProceso(entidadJPA);
                managerDAO.edit(entidadPadreJPA);
            }
            LogAuditoria logAud_ = new LogAuditoria("EJECUCIONPROCESO", Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), request.getUserName(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud_);

            // 4 Todo en orden, retornando
            ResponseWrapper wraper = new ResponseWrapper(true, I18N.getMessage("resultados.aprovacion"), response);
            return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            Conciliacion _entidadPadre = managerDAO.find(entidadPadre.getId());
            EjecucionProceso _logAud = new EjecucionProceso();
            _logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            _logAud.setConciliacion(null);
            _logAud.setNombre("EJECUTADA:" + entidadPadre.getNombre());
            _logAud.setEstadoEjecucion("FALLIDA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
            _logAud.setFechaEjecucion(new Date());
            _logAud.setNombreConciliacion(entidadPadre.getNombre());
            _logAud.setRespuesta(e.toString());

            System.out.println("INTEGRACIÓN FALLIDA: " + e.toString());
            System.out.println("----------");
            logEjecucionDAO.create(_logAud);
            _logAud.setConciliacion(_entidadPadre);
            logEjecucionDAO.edit(_logAud);
            _entidadPadre.addEjecucionProceso(_logAud);
            managerDAO.edit(_entidadPadre);

            // 4 Falló
            if (e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, e.getCause().getMessage(), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            } else {
                logger.log(Level.SEVERE, e.getMessage(), e);
                ResponseWrapper wraper = new ResponseWrapper(false, e.getMessage(), 500);
                return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
            }
        }
    }

    @POST
    @Path("/cancelar")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response cancelar(WsTransformacionDTO request) {
        // 1. Registrar log de eventos para inicio de integración
        Conciliacion entidadPadre = managerDAO.find(request.getIdConciliacion());
        EjecucionProceso logAud = new EjecucionProceso();
        logAud.setComponenteEjecutado("INTEGRACION_ODI"); // TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
        logAud.setConciliacion(null);
        logAud.setEstadoEjecucion("CANCELADA");// TODO: VALIDAR SI EXISTE ENUMERACIÓN O ALGO DEFINIDO
        logAud.setFechaEjecucion(new Date());
        logAud.setNombre("INICIADA:" + entidadPadre.getNombre());
        logAud.setNombreConciliacion(entidadPadre.getNombre());

        /*    logEjecucionDAO.create(logAud);
        logAud.setConciliacion(entidadPadre);
        logEjecucionDAO.edit(logAud);
        entidadPadre.addEjecucionProceso(logAud);
        managerDAO.edit(entidadPadre);*/
        // 2.1 Traer parámetros
        String wsdlLocation;
        try {
            wsdlLocation = parametroDAO.findByParametro("SISTEMA", "V_odiUrl");
        } catch (Exception e) {
            wsdlLocation = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
        }

        String odiUsuario;
        try {
            String _odiUsuario = parametroDAO.findByParametro("SEGURIDAD", "V_odiUsuario");
            odiUsuario = Crypto.cryptoParam(_odiUsuario);
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

                if (responses.size() > 0) {
                    String estado = responses.get(0).getLoadPlanStatus();
                    if (estado == "Q" || estado == "R" || estado == "W") {
                        // Ya está corriendo por tanto no puede cancelarlo
                        OdiStopLoadPlanType stopLoadPlan = fachadaOdi.stopLoadPlan(wsdlLocation, odiUsuario, odiPassword, odiWorkRepository, idPlan, 1, "IMMEDIATE");
                        ResponseWrapper wraper = new ResponseWrapper(true, I18N.getMessage("odiinvoke.execute"), stopLoadPlan);
                        return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                    } else {
                        // Está en un etado que no permite cancelación
                        ResponseWrapper wraper = new ResponseWrapper(false, "El estado (" + estado + ") actual de " + entidadPadre.getNombre() + " no permite cancelación!", 500);
                        return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                    }
                } else {
                    // No hay  nada que cancelar
                    ResponseWrapper wraper = new ResponseWrapper(false, "No se puede cancelar, dado que " + entidadPadre.getNombre() + " no se encuentra en ejecución.", 500);
                    return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                }

            } catch (Exception e) {
                if (e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                    ResponseWrapper wraper = new ResponseWrapper(false, e.getCause().getMessage(), 500);
                    return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                } else {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                    ResponseWrapper wraper = new ResponseWrapper(false, e.getMessage(), 500);

                    return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
                }
            }
        } else {
            // No hay  nada que cancelar
            ResponseWrapper wraper = new ResponseWrapper(false, "No se puede cancelar, dado que " + entidadPadre.getNombre() + " no se encuentra en ejecución.", 500);
            return Response.ok(wraper, MediaType.APPLICATION_JSON).build();
        }

    }

}
