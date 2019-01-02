package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.EscenarioDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.i18n.I18N;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;
import co.com.claro.service.rest.util.ResponseWrapper;

import java.time.Instant;
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
 * Clase que maneja el API Rest de Escenarios
 *
 * @author Andres Bedoya
 */
@Stateless
@Path("escenarios")
public class EscenarioREST {

    @Transient
    private static final Logger logger = Logger.getLogger(EscenarioREST.class.getSimpleName());

    private String modulo = "ESCENARIOS";

    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;

    @EJB
    protected EscenarioDAO managerDAO;

    @EJB
    protected ConciliacionDAO padreDAO;

    @EJB
    protected ParametroDAO parametroDAO;

    /**
     * Obtiene las Escenarios Paginadas
     *
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id,
     * nombre, fechaCreacion)
     * @return Toda la lista de escenarios que corresponden con el criterio
     */
    @GET
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<EscenarioDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby,
            @QueryParam("name") String name) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});
        
        List<Escenario> lst = null;
        if(name == null) {
        	lst = managerDAO.findRange(new int[]{offset, limit});
        }else {
        	lst = managerDAO.findByName(name);
        }
        
        List<PadreDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(EscenarioDTO::getId).reversed()).collect(toList());

        lstDTO = UtilListas.ordenarLista(lstDTO, orderby);
        List<EscenarioDTO> lstFinal = (List<EscenarioDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una Escenario por id
     *
     * @param id Identificador de conciliacion
     * @return Una Escenario que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public EscenarioDTO getById(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        Escenario entidad = managerDAO.find(id);
        return entidad.toDTO();

    }

    /**
     * Busca las escenarios por cualquier columna
     *
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Escenarios que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<EscenarioDTO> findByAnyColumn(@QueryParam("texto") String texto) {
        logger.log(Level.INFO, "texto:{0}", texto);
        List<Escenario> lst = managerDAO.findByAnyColumn(texto);
        List<PadreDTO> lstDTO = lst.stream().map(item -> item.toDTO()).sorted(comparing(EscenarioDTO::getId)).collect(toList());
        List<EscenarioDTO> lstFinal = (List<EscenarioDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una Escenario por id
     *
     * @param id Identificador de conciliacion
     * @return Una QueryEscenario que coincide con el criterio
     */
    @GET
    @Path("/conciliacion/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<EscenarioDTO> getByConciliacion(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        List<EscenarioDTO> lstDTO;
        List<Escenario> lst;
        lst = managerDAO.findByConciliacion(id);
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(EscenarioDTO::getId)).collect(toList());
        List<EscenarioDTO> lstFinal = (List<EscenarioDTO>) (List<?>) lstDTO;
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
    public Response add(EscenarioDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);
        
        try {
        	Conciliacion entidadPadreJPA;
            Escenario entidadJPA = dto.toEntity();
            entidadPadreJPA = padreDAO.find(dto.getIdConciliacion());
            if (entidadPadreJPA != null) {
                managerDAO.create(entidadJPA);
                entidadJPA.setConciliacion(entidadPadreJPA);
                managerDAO.edit(entidadJPA);
                entidadPadreJPA.addEscenario(entidadJPA);
                padreDAO.edit(entidadPadreJPA);
            }
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), dto.getUsername(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud);

        	ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("escenario.save", entidadJPA.getNombre()), entidadJPA.toDTO());
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
     * Actualiza la entidad por su Id
     *
     * @param entidad conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(EscenarioDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        
        try {
        	Conciliacion entidadPadreJPA = null;
            if (entidad.getIdConciliacion() != null) {
                entidadPadreJPA = padreDAO.find(entidad.getIdConciliacion());
                if (entidadPadreJPA == null) {
                    throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidad.getIdConciliacion());
                }
            }
            //Hallar La entidad actual para actualizarla
            Escenario entidadJPA = managerDAO.find(entidad.getId());
            if (entidadJPA != null) {
                entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
                entidadJPA.setNombre(entidad.getNombre() != null ? entidad.getNombre() : entidadJPA.getNombre());
                entidadJPA.setImpacto(entidad.getImpacto() != null ? entidad.getImpacto() : entidadJPA.getImpacto());
                entidadJPA.setDescripcion(entidad.getDescripcion() != null ? entidad.getDescripcion() : entidadJPA.getDescripcion());
                entidadJPA.setConciliacion(entidad.getIdConciliacion() != null ? (entidadPadreJPA != null ? entidadPadreJPA : null) : entidadJPA.getConciliacion());
                managerDAO.edit(entidadJPA);
                if ((entidadPadreJPA != null)) {
                    entidadPadreJPA.addEscenario(entidadJPA);
                    padreDAO.edit(entidadPadreJPA);
                }
                LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
                logAuditoriaDAO.create(logAud);
                
                ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("escenario.update", entidad.getNombre()) ,entidadJPA.toDTO());
            	return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
            }
            
            ResponseWrapper wraper = new ResponseWrapper(false,I18N.getMessage("escenario.notfound", entidad.getNombre()));
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
        	
        	
        	Escenario entidadJPA = managerDAO.find(id);
            parametroDAO.validarExistenciaParametro("ESCENARIO", entidadJPA.getId());
            EscenarioDTO dto = entidadJPA.toDTO();
            Conciliacion entidadPadreJPA = null;
            if (entidadJPA.getConciliacion() != null) {
                entidadPadreJPA = padreDAO.find(entidadJPA.getConciliacion().getId());
                entidadPadreJPA.removeEscenario(entidadJPA);
            }
            managerDAO.remove(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, dto.toString());
            logAuditoriaDAO.create(logAud);
            if (entidadPadreJPA != null) {
                padreDAO.edit(entidadPadreJPA);
            }
        	ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("escenario.delete", entidadJPA.getNombre()));
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
}
