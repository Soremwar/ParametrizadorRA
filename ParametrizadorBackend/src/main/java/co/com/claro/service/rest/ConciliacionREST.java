package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.IWsTransformacionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.EscenarioDTO;
import co.com.claro.model.dto.WsTransformacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Politica;
import co.com.claro.model.entity.WsTransformacion;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.i18n.I18N;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;
import co.com.claro.service.rest.util.ResponseWrapper;

import java.time.Instant;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.Date;
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
    @Produces({MediaType.APPLICATION_JSON+";charset=utf-8"})
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
            /*if (transformacionDAO.findByPaqueteWs(dto.getPaquete()) != null) {
                //throw new DataAlreadyExistException(Response.Status.NOT_ACCEPTABLE.getReasonPhrase() +  dto.getPaquete());
                WrapperResponseEntity response = new WrapperResponseEntity(ResponseCode.CONFLICT,  "Informacion ya existe", "El paquete " + dto.getPaquete() + " ya existe");
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
            }*/
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
        	
        	ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("conciliaciones.save", entidadJPA.getNombre()), entidadJPA.toDTO());
        	return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
        }catch (Exception e) {
        	if(e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
        		logger.log(Level.SEVERE, e.getMessage(), e);
        		ResponseWrapper wraper = new ResponseWrapper(false,  e.getCause().getMessage(), 500);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
        	}else {
        		logger.log(Level.SEVERE, e.getMessage(), e);
        		ResponseWrapper wraper = new ResponseWrapper(false,  I18N.getMessage("general.readerror"), 500);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
        	}
        	
        }
    }

    public void crearPaquete(ConciliacionDTO dto, Conciliacion entidadJPA) {
        WsTransformacion transformacion = new WsTransformacion();
        transformacion.setFechaCreacion(Date.from(Instant.now()));
        transformacion.setNombreWs(dto.getPaquete());
        transformacion.setPaqueteWs(dto.getPaquete());
        transformacion.setConciliacion(entidadJPA);
        entidadJPA.addTransformacion(transformacion);
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
            List<WsTransformacion> results = transformacionDAO.validPaqueteWs(entidadDTO.getPaquete());
            String paquete = (results != null && !results.isEmpty()) ? results.get(0).getPaqueteWs() : "";
            //System.out.println("paquete abp" + paquete);
            if (entidadDTO.getIdPolitica() != null) {
                entidadPadreJPA = padreDAO.find(entidadDTO.getIdPolitica());
                if (entidadPadreJPA == null) {
                    throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidadDTO.getIdPolitica());
                }
            }
            //Hallar La entidadDTO actual para actualizarla
            Conciliacion entidadJPA = managerDAO.find(entidadDTO.getId());
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
                
                ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("conciliaciones.update", entidadJPA.getNombre()) ,entidadDTO);
            	return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
            }
            
            ResponseWrapper wraper = new ResponseWrapper(false,I18N.getMessage("conciliaciones.notfound", entidadJPA.getNombre()));
        	return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
        	
        }catch (Exception e) {
        	if(e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
        		logger.log(Level.SEVERE, e.getMessage(), e);
        		ResponseWrapper wraper = new ResponseWrapper(false,  e.getCause().getMessage(), 500);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
        	}else {
        		logger.log(Level.SEVERE, e.getMessage(), e);
        		ResponseWrapper wraper = new ResponseWrapper(false,  I18N.getMessage("general.readerror"), 500);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
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
            managerDAO.remove(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, dto.toString());
            logAuditoriaDAO.create(logAud);
            if (entidadPadreJPA != null) {
                padreDAO.edit(entidadPadreJPA);
            }
    		ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("conciliaciones.delete"), entidadJPA.getNombre());
    		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    	}catch (Exception e) {
    		if(e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
        		logger.log(Level.SEVERE, e.getMessage(), e);
        		ResponseWrapper wraper = new ResponseWrapper(false,  e.getCause().getMessage(), 500);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
        	}else {
        		logger.log(Level.SEVERE, e.getMessage(), e);
        		ResponseWrapper wraper = new ResponseWrapper(false,  I18N.getMessage("general.readerror"), 500);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
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
            
    		ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("programaciones.programar") ,entidadJPA.toDTO());
    		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    	}catch (Exception e) {
    		if(e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
    			logger.log(Level.SEVERE, e.getMessage(), e);
    			ResponseWrapper wraper = new ResponseWrapper(false,  e.getCause().getMessage(), 500);
    			return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}else {
    			logger.log(Level.SEVERE, e.getMessage(), e);
    			ResponseWrapper wraper = new ResponseWrapper(false,  I18N.getMessage("general.readerror"), 500);
    			return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}
    	}
        
    }

}
