package co.com.claro.service.rest;


import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.IWsTransformacionDAO;
import co.com.claro.ejb.dao.WsTransformacionDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.WsTransformacionDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.WsTransformacion;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Clase que maneja el API Rest de WsTransformaciones
 * @author Andres Bedoya
 */
@Path("wstransformacion")
public class WsTransformacionREST{
    @Transient
    private static final Logger logger = Logger.getLogger(WsTransformacionREST.class.getSimpleName());
   
    @EJB
    protected IWsTransformacionDAO managerDAO;
    
    @EJB
    protected ConciliacionDAO padreDAO;

    /**
     * Obtiene las Resultados Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de resultados que corresponden con el criterio
     */
    @GET
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<WsTransformacionDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
        List<WsTransformacion> lst = managerDAO.findRange(new int[]{offset, limit});
        List<WsTransformacionDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().collect(toList());
        //UtilListas.ordenarListaTransormacion(lstDTO, orderby);
        List<WsTransformacionDTO> lstFinal = (List<WsTransformacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }   
    
    /**
     * Retorna la entidad x id
     * @param id identificador de la entidad
     * @return Retorna la entidad x id
     */
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public WsTransformacionDTO getById(@PathParam("id") Integer id){
        logger.log(Level.INFO, "id:{0}", id);
        WsTransformacion entidad = managerDAO.find(id);
        return entidad.toDTO();

    }

     /**
     * Busca los resultados por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de WsTransformaciones que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<WsTransformacionDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);        
        List<WsTransformacion> lst = managerDAO.findByAnyColumn(texto);
        List<WsTransformacionDTO> lstDTO = new ArrayList<>();        
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<WsTransformacionDTO> lstFinal = (List<WsTransformacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }
   
    /**
     * Crea una nueva entidad
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(WsTransformacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Conciliacion entidadPadreJPA;
        WsTransformacion entidadHijaJPA = entidad.toEntity();
        entidadHijaJPA.setConciliacion(null);        

        if ( entidad.getIdConciliacion() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdConciliacion());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException("Datos no encontrados " + entidad.getIdConciliacion());
            } else {
                managerDAO.create(entidadHijaJPA);
                entidadHijaJPA.setConciliacion(entidadPadreJPA);
                managerDAO.edit(entidadHijaJPA);
                entidadPadreJPA.addTransformacion(entidadHijaJPA);
                padreDAO.edit(entidadPadreJPA);
            }
        } else {
            managerDAO.create(entidadHijaJPA);
        }
        return Response.status(Response.Status.CREATED).entity(entidadHijaJPA.toDTO()).build();
    }
    
    /**
     * Actualiza la entidad por su Id
     * @param entidad conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(WsTransformacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        Conciliacion entidadPadreJPA = null;
        if (entidad.getIdConciliacion() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdConciliacion());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidad.getIdConciliacion());
            }
        }
        //Hallar La entidad actual para actualizarla
        WsTransformacion entidadHijaJPA = managerDAO.find(entidad.getId());
        if (entidadHijaJPA != null) {
            entidadHijaJPA.setFechaActualizacion(Date.from(Instant.now()));
            entidadHijaJPA.setNombreWs(entidad.getNombreWs()!= null ? entidad.getNombreWs(): entidadHijaJPA.getNombreWs());
            entidadHijaJPA.setPaqueteWs(entidad.getPaqueteWs()!= null ? entidad.getPaqueteWs(): entidadHijaJPA.getPaqueteWs());
            entidadHijaJPA.setConciliacion(entidad.getIdConciliacion() != null ?  (entidadPadreJPA != null ? entidadPadreJPA : null): entidadHijaJPA.getConciliacion());
            managerDAO.edit(entidadHijaJPA);
            if ((entidadPadreJPA != null)){
                entidadPadreJPA.addTransformacion(entidadHijaJPA);
                padreDAO.edit(entidadPadreJPA);
            }
            return Response.status(Response.Status.OK).entity(entidadHijaJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
     /**
     * Borra una conciliacion por su Id
     * @param id Identificador de la identidad
     * @return el resultado de la operacion
     */
    @DELETE
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id) {
        WsTransformacion hijo = managerDAO.find(id);
        Conciliacion entidadPadreJPA = null;
        if (hijo.getConciliacion() != null) {
            entidadPadreJPA = padreDAO.find(hijo.getConciliacion().getId());
            entidadPadreJPA.removeTransformacion(hijo);
        }
        managerDAO.remove(hijo);
        if (entidadPadreJPA != null) {
            padreDAO.edit(entidadPadreJPA);
        }
        WrapperResponseEntity mensaje = new WrapperResponseEntity(Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase(), "Registro borrado exitosamente");
        return Response.status(Response.Status.OK).entity(mensaje).build();
    }
    /**
     * Retorna el numero de registros 
     * @return numero de registros total
     */
    @GET
    @Path("/count")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }
 
}
