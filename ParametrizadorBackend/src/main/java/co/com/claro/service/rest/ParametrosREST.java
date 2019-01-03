/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.CredencialesDTO;
import co.com.claro.model.dto.ParametroDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Parametro;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.i18n.I18N;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeededFilter;
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
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andresbedoya
 */
@Stateless
@Path("parametros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ParametrosREST {

    @Transient
    private static final Logger logger = Logger.getLogger(ParametrosREST.class.getSimpleName());
    private String modulo = "PARAMETROS";
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;
    @EJB
    protected ParametroDAO managerDAO;
    
    @EJB
    protected EscenarioDAO escenarioDAO;
    
    @EJB
    private ConciliacionDAO conciliacionDAO;
    
    /**
     * Obtiene las Parametros Paginadas
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
    public List<ParametroDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2} ", new Object[]{offset, limit, orderby});
        List<Parametro> lst = managerDAO.findRange(new int[]{offset, limit});
        List<ParametroDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(ParametroDTO::getId).reversed()).collect(toList());
        //lstDTO = UtilListas.ordenarListaParametros(lstDTO, orderby);
        List<ParametroDTO> lstFinal = (List<ParametroDTO>) (List<?>) lstDTO;
        return lstFinal;
    }
    
    @GET
    @Path("/returnSeed")
    @Produces({MediaType.APPLICATION_JSON})
    public ParametroDTO returnSeed() {
       Parametro response = new Parametro();
       String seed = JWTTokenNeededFilter.KEYPAR;
       String asc = "";
       for(int i=0; i<seed.length(); i++) {
    	   char c = seed.charAt (i); 
    	   int ascii = (int) c;
    	   ascii = ascii - 3;
    	   asc = asc + (char)ascii;     	  
       }       
       response.setValor(asc);
       return response.toDTO();
    }
    
    @POST
    @Path("/ldapURL")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ParametroDTO returnldap(CredencialesDTO credentials) {
       Parametro response = new Parametro();
       String ldapUrl = "";
       String credentialUrl = "";
       
       if(credentials.getCommonName().trim().equals("*")) {
      	 ldapUrl = "ldap://" + credentials.getIp().trim() + ":" + credentials.getPort().trim() + "/" + credentials.getDomainGroup().trim();	
       }else {		        	 
      	 ldapUrl = "ldap://" + credentials.getIp().trim() + ":" + credentials.getPort().trim() + "/" + credentials.getCommonName().trim() + "," + credentials.getDomainGroup().trim();			        	 
       }      
       if(credentials.getOrganization().trim().equals("*")) {
      	 credentialUrl = "uid=" + credentials.getUserName().trim() + "," + credentials.getDomainGroup().trim();
       }else {
      	 credentialUrl = "uid=" + credentials.getUserName().trim() + ",ou=" + credentials.getOrganization().trim() + "," +credentials.getDomainGroup().trim();
       }     
       response.setValor("url: "+ ldapUrl + " busqueda: " + credentialUrl);
              
       return response.toDTO();
    }

    @GET
    @Path("/findAVencer")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ParametroDTO> findAVencer() {
        List<Parametro> lst = managerDAO.findAVencer();
        return lst.stream().map(item->item.toDTO()).collect(toList());
    }

    /**
     * Obtiene un indicador por id
     *
     * @param id Identificador de conciliacion
     * @return Una Escenario que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public ParametroDTO getById(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        Parametro entidad = managerDAO.find(id);
        
        ParametroDTO dto = entidad.toDTO();
        
        if("ESCENARIO".equals(dto.getTipo())) {
        	Escenario escenario = escenarioDAO.find(dto.getCodPadre());
        	dto.setCodPadreDesc(escenario.getNombre());
        }else if("CONCILIACION".equals(dto.getTipo())) {
        	Conciliacion conciliacion = conciliacionDAO.find(dto.getCodPadre());
        	dto.setCodPadreDesc(conciliacion.getNombre());
        }
        
        return dto;

    }

    /**
     * Busca los indicadores por cualquier columna
     *
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Escenarios que cumplen con el criterio
     */
    @GET
    @Path("findByAny")
    public List<ParametroDTO> findByAnyColumn(@QueryParam("texto") String texto) {
    	try {
    		logger.log(Level.INFO, "texto:{0}", texto);
            List<Parametro> lst = managerDAO.findByAnyColumn(texto);
            List<ParametroDTO> lstDTO = lst.stream().map(item -> item.toDTO()).sorted(comparing(ParametroDTO::getId)).collect(toList());
            List<ParametroDTO> lstFinal = (List<ParametroDTO>) (List<?>) lstDTO;
            return lstFinal;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        
    }
    
//    @OPTIONS
//    @Path("findByAny")
//    public Response findByAnyColumnOption() {
//       return Response.ok().header(HttpHeaders.ALLOW, HttpMethod.POST).build();
//    }

    /**
     * Busca los Parametros por algun padre. Recuerden que la relacion es logica
     * no fisica
     *
     * @param tipo
     * @param codPadre
     * @return Lista de Escenarios que cumplen con el criterio
     */
    @GET
    @Path("/padre")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<ParametroDTO> findByPadre(@QueryParam("tipo") String tipo, @QueryParam("codpadre") Integer codPadre) {
        logger.log(Level.INFO, "tipo:{0}codPadre:{1}", new Object[]{tipo, codPadre});
        List<Parametro> lst = managerDAO.findByCodPadre(tipo, codPadre);
        List<ParametroDTO> lstDTO = lst.stream().map(item -> item.toDTO()).sorted(comparing(ParametroDTO::getId)).collect(toList());
        List<ParametroDTO> lstFinal = (List<ParametroDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Crea una nueva entidad
     *
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(ParametroDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        
        try {
        	
        	Parametro entidadJPA = entidad.toEntity();
            managerDAO.create(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud);
        	
        	ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("parametros.save", entidad.getParametro()) ,entidadJPA.toDTO());
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
    public Response update(ParametroDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        try {
            Parametro entidadJPA = managerDAO.find(entidad.getId());
            if (entidadJPA != null) {
                entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
                entidadJPA.setParametro(entidad.getParametro() != null ? entidad.getParametro() : entidadJPA.getParametro());
                entidadJPA.setValor(entidad.getValor() != null ? entidad.getValor() : entidadJPA.getValor());
                entidadJPA.setDescripcion(entidad.getDescripcion() != null ? entidad.getDescripcion() : entidadJPA.getDescripcion());
                entidadJPA.setTipo(entidad.getTipo() != null ? entidad.getTipo() : entidadJPA.getTipo());
                entidadJPA.setCodPadre(entidad.getCodPadre() != null ? entidad.getCodPadre() : entidadJPA.getCodPadre());
                managerDAO.edit(entidadJPA);
                LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
                logAuditoriaDAO.create(logAud);
                ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("parametros.update", entidadJPA.getParametro()),entidadJPA.toDTO() );
            	return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
            }
        	ResponseWrapper wraper = new ResponseWrapper(false,I18N.getMessage("parametros.notfound", entidadJPA.getParametro()) );
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
     * Borra una entidad por su Id
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
    		Parametro entidadJPA = managerDAO.find(id);
            ParametroDTO dto = entidadJPA.toDTO();
            managerDAO.remove(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, dto.toString());
            logAuditoriaDAO.create(logAud);
            WrapperResponseEntity mensaje = new WrapperResponseEntity(Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase(), "Registro borrado exitosamente");
            
    		ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("parametros.delete", entidadJPA.getParametro()) ,mensaje);
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
