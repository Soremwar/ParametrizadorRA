package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.response.ResponseCode;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;
import co.com.claro.service.rest.util.ResponseWrapper;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.i18n.I18N;
import co.com.claro.service.rest.parent.AbstractParentREST;
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
import javax.persistence.PersistenceException;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Clase que maneja el API Rest de Politicas
 * @author Andres Bedoya
 */
@Stateless
@Path("politicas")
public class PoliticaREST extends AbstractParentREST<PoliticaDTO>{
    @Transient
    private static final Logger logger = Logger.getLogger(PoliticaREST.class.getSimpleName());
    
    private String modulo = "POLITICAS";
    
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;
    
    @EJB
    protected PoliticaDAO managerDAO;
    
    @EJB
    protected ConciliacionDAO conciliacionDAO;

    @GET
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
    	try {
    		List<Politica> lst = managerDAO.findRange(new int[]{offset, limit});
            List<PadreDTO> lstDTO = lst.stream().map(item -> (item.toDTO())).distinct().sorted(comparing(PadreDTO::getId)).collect(toList());
            List<PoliticaDTO> lstFinal = (List<PoliticaDTO>)(List<?>) lstDTO;
            ResponseWrapper wraper = new ResponseWrapper(true,lstFinal);
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
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") Integer id){
    	logger.log(Level.INFO, "id:{0}", id);
    	try {
            Politica entidad = managerDAO.findByAllTreeById(id);
            ResponseWrapper wraper = new ResponseWrapper(true,entidad.toDTO());
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
    @Path("/findPoliticasSinConciliacion")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<PoliticaDTO> findPoliticasSinConciliacion(@QueryParam("name") String name){
    	
        List<Politica> lst = managerDAO.findPoliticaSinConciliacion(name);
        List<PadreDTO> lstDTO = new ArrayList<>();        
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<PoliticaDTO> lstFinal = (List<PoliticaDTO>)(List<?>) lstDTO;
        return lstFinal;
    }

    @GET
    @Path("/findByAny")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<PoliticaDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);        
        List<Politica> lst = managerDAO.findByAnyColumn(texto);
        List<PadreDTO> lstDTO = new ArrayList<>();        
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<PoliticaDTO> lstFinal = (List<PoliticaDTO>)(List<?>) lstDTO;
        return lstFinal;
    }
   
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(PoliticaDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);
        try {
            Politica entidadJPA = dto.toEntity();
            managerDAO.create(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), dto.getUsername(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud);
            ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("politicas.save", entidadJPA.getNombre()), entidadJPA.toDTO());
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
    public Response update(PoliticaDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);  
        //Hallar La dto actual para actualizarla
        logger.log(Level.INFO, "entidad:{0}", dto);
        try {
			 Politica entidadJPA = managerDAO.find(dto.getId());
		     if (entidadJPA != null) {
				entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
				entidadJPA.setNombre(dto.getNombre() != null ? dto.getNombre() : entidadJPA.getNombre());
				entidadJPA.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion() : entidadJPA.getDescripcion());
				entidadJPA.setObjetivo(dto.getObjetivo() != null ? dto.getObjetivo() : entidadJPA.getObjetivo());
				managerDAO.edit(entidadJPA);
				LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), dto.getUsername(), entidadJPA.toString());
				logAuditoriaDAO.create(logAud);
				ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("politicas.update", entidadJPA.getNombre()), entidadJPA.toDTO());
				return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
		    }else {
		    	ResponseWrapper wraper = new ResponseWrapper(false,  I18N.getMessage("politicas.notfound"), 500);
	        	return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
		    }
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
     * @param id Identificador de la entidadJPA
     * @return El resultado de la operacion en codigo HTTP
     */
    @DELETE
    @Path("{id}/{username}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public Response remove(@PathParam("id") Integer id, @PathParam("username") String username) {
        try {
        	Politica entidadJPA = managerDAO.find(id);
            PoliticaDTO dto = entidadJPA.toDTO();
            managerDAO.remove(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, dto.toString());
            logAuditoriaDAO.create(logAud);
            WrapperResponseEntity mensaje = new WrapperResponseEntity(Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase(), "Registro borrado exitosamente");
        	ResponseWrapper wraper = new ResponseWrapper(true, I18N.getMessage("politicas.delete", entidadJPA.getNombre()), mensaje);
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
    public int count(){
        return managerDAO.count();
    }

}
