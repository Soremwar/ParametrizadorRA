/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.QueryEscenarioDAO;
import co.com.claro.model.dto.QueryEscenarioDTO;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.QueryEscenario;
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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andresbedoya
 */
@Stateless
@Path("queryescenario")
public class QueryEscenarioREST {

    @Transient
    private static final Logger logger = Logger.getLogger(QueryEscenarioREST.class.getSimpleName());
   
    private String modulo = "QUERYESCENARIO";

    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;
    @EJB
    protected QueryEscenarioDAO managerDAO;

    @EJB
    protected EscenarioDAO padreDAO;

    /**
     * Obtiene las QueryEscenarioes Paginadas
     *
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id,
     * nombre, fechaCreacion)
     * @param texto
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<QueryEscenarioDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby,
            @QueryParam("texto") String texto) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}texto:{3}", new Object[]{offset, limit, orderby, texto});
        List<QueryEscenarioDTO> lstDTO;
        List<QueryEscenario> lst;
        if (texto != null && !texto.isEmpty()) {
            lst = managerDAO.findByAnyColumn(texto);
        } else {
            lst = managerDAO.findRange(null);
        }
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(QueryEscenarioDTO::getId)).collect(toList());
        //UtilListas.ordenarQueryEjecucion(lstDTO, orderby);
        List<QueryEscenarioDTO> lstFinal = (List<QueryEscenarioDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una QueryEscenario por id
     *
     * @param id Identificador de conciliacion
     * @return Una QueryEscenario que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public QueryEscenarioDTO getById(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        QueryEscenario entidad = managerDAO.find(id);
        return entidad.toDTO();
    }

    /**
     * Obtiene una QueryEscenario por id
     *
     * @param id Identificador de conciliacion
     * @return Una QueryEscenario que coincide con el criterio
     */
    @GET
    @Path("/conciliacion/{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<QueryEscenarioDTO> getByIdConciliacion(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        List<QueryEscenarioDTO> lstDTO;
        List<QueryEscenario> lst;
        lst = managerDAO.findByConciliacion(id);
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(QueryEscenarioDTO::getId)).collect(toList());
        List<QueryEscenarioDTO> lstFinal = (List<QueryEscenarioDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

        /**
     * Obtiene una QueryEscenario por id
     *
     * @param id Identificador de conciliacion
     * @return Una QueryEscenario que coincide con el criterio
     */
    @GET
    @Path("/escenario/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<QueryEscenarioDTO> getByIdEscenario(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        List<QueryEscenarioDTO> lstDTO;
        List<QueryEscenario> lst;
        lst = managerDAO.findByEscenario(id);
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(QueryEscenarioDTO::getId)).collect(toList());
        List<QueryEscenarioDTO> lstFinal = (List<QueryEscenarioDTO>) (List<?>) lstDTO;
        return lstFinal;
    }
    
    /**
     * Crea una nueva politica
     *
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(QueryEscenarioDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        
        try {
        	Escenario entidadPadreJPA;
            QueryEscenario entidadJPA = entidad.toEntity();
            entidadJPA.setEscenario(null);
            if (entidad.getIdEscenario() != null) {
                entidadPadreJPA = padreDAO.find(entidad.getIdEscenario());
                if (entidadPadreJPA == null) {
                    throw new DataNotFoundException("Datos no encontrados " + entidad.getIdEscenario());
                } else {
                    managerDAO.create(entidadJPA);
                    entidadJPA.setEscenario(entidadPadreJPA);
                    managerDAO.edit(entidadJPA);
                    entidadPadreJPA.addQueryEscenario(entidadJPA);
                    padreDAO.edit(entidadPadreJPA);
                }
            } else {
                managerDAO.create(entidadJPA);
            }
            managerDAO.edit(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud);

        	ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("queries.save", entidadJPA.getNombreQuery()) ,entidadJPA.toDTO());
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

    @PUT
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(QueryEscenarioDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        try {
        	
        	Escenario entidadPadreJPA = null;
            if (entidad.getIdEscenario() != null) {
                entidadPadreJPA = padreDAO.find(entidad.getIdEscenario());
                if (entidadPadreJPA == null) {
                    throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidad.getIdEscenario());
                }
            }

            //Hallar La entidad actual para actualizarla
            QueryEscenario entidadJPA = managerDAO.find(entidad.getId());
            if (entidadJPA != null) {
                entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
                entidadJPA.setNombreQuery(entidad.getNombreQuery() != null ? entidad.getNombreQuery() : entidadJPA.getNombreQuery());
                entidadJPA.setOrden(entidad.getOrden() != null ? entidad.getOrden() : entidadJPA.getOrden());
                entidadJPA.setQuery(entidad.getQuery() != null ? entidad.getQuery() : entidadJPA.getQuery());
                entidadJPA.setEscenario(entidad.getIdEscenario() != null ? (entidadPadreJPA != null ? entidadPadreJPA : null) : entidadJPA.getEscenario());
                managerDAO.edit(entidadJPA);
                LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
                logAuditoriaDAO.create(logAud);
                
                ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("queries.update", entidadJPA.getNombreQuery()) ,entidadJPA.toDTO());
            	return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
            }
            
            ResponseWrapper wraper = new ResponseWrapper(false,I18N.getMessage("queries.notfound", entidadJPA.getNombreQuery()) ,entidadJPA.toDTO());
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
     * Borra una politica por su Id
     *
     * @param id Identificador de la entidad
     * @return El resultado de la operacion en codigo HTTP
     */
    @DELETE
    @Path("{id}/{username}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id, @PathParam("username") String username) {
    	try {
    		QueryEscenario entidadJPA = managerDAO.find(id);
            QueryEscenarioDTO dto = entidadJPA.toDTO();
            Escenario entidadPadreJPA = null;
            if (entidadJPA.getEscenario() != null) {
                entidadPadreJPA = padreDAO.find(entidadJPA.getEscenario().getId());
                entidadPadreJPA.removeIndicador(entidadJPA);
            }
            managerDAO.remove(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, dto.toString());
            logAuditoriaDAO.create(logAud);

            if (entidadPadreJPA != null) {
                padreDAO.edit(entidadPadreJPA);
            }
    		ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("queries.delete", entidadJPA.getNombreQuery()), null );
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
