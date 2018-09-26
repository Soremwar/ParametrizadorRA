/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.QueryEscenarioDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.QueryEscenarioDTO;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.QueryEscenario;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.response.WrapperResponseEntity;
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
    
    @EJB
    protected QueryEscenarioDAO managerDAO;
    
    @EJB
    protected EscenarioDAO padreDAO;

    /**
     * Obtiene las QueryEscenarioes Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @param texto
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
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
            lst = managerDAO.findRange(new int[]{offset, limit});
        }
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(QueryEscenarioDTO::getId)).collect(toList());
        UtilListas.ordenarQueryEjecucion(lstDTO, orderby);
        List<QueryEscenarioDTO> lstFinal = (List<QueryEscenarioDTO>)(List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una QueryEscenario por id
     * @param id Identificador de conciliacion
     * @return Una QueryEscenario que coincide con el criterio
     */
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public QueryEscenarioDTO getById(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}" , id);
        QueryEscenario entidad = managerDAO.find(id);
        return entidad.toDTO();
    }
    
    
        /**
     * Obtiene una QueryEscenario por id
     * @param id Identificador de conciliacion
     * @return Una QueryEscenario que coincide con el criterio
     */
    
    @GET
    @Path("/conciliacion/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<QueryEscenarioDTO> getByIdConciliacion(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}" , id);
        List<QueryEscenarioDTO> lstDTO;
        List<QueryEscenario> lst;
        lst = managerDAO.findByConciliacion(id);
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(QueryEscenarioDTO::getId)).collect(toList());
        List<QueryEscenarioDTO> lstFinal = (List<QueryEscenarioDTO>)(List<?>) lstDTO;
        return lstFinal;
    }
    

     /**
     * Crea una nueva politica
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(QueryEscenarioDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Escenario entidadPadreJPA;
        QueryEscenario entidadHijaJPA = entidad.toEntity();
        entidadHijaJPA.setEscenario(null);
        if ( entidad.getIdEscenario() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdEscenario());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException("Datos no encontrados " + entidad.getIdEscenario());
            } else {
                managerDAO.create(entidadHijaJPA);
                entidadHijaJPA.setEscenario(entidadPadreJPA);
                managerDAO.edit(entidadHijaJPA);
                entidadPadreJPA.addQueryEscenario(entidadHijaJPA);
                padreDAO.edit(entidadPadreJPA);
            }
        } else {
            managerDAO.create(entidadHijaJPA);
        }
        return Response.status(Response.Status.CREATED).entity(entidadHijaJPA.toDTO()).build();
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(QueryEscenarioDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        //Hallar La entidad actual para actualizarla
        QueryEscenario queryEscenarioJPA = managerDAO.find(entidad.getId());
        if (queryEscenarioJPA != null) {
            queryEscenarioJPA.setFechaActualizacion(Date.from(Instant.now()));
            queryEscenarioJPA.setNombreQuery(entidad.getNombreQuery() != null ? entidad.getNombreQuery() : queryEscenarioJPA.getNombreQuery());
            queryEscenarioJPA.setOrden(entidad.getOrden() != null ? entidad.getOrden() : queryEscenarioJPA.getOrden());
            queryEscenarioJPA.setQuery(entidad.getQuery() != null ? entidad.getQuery() : queryEscenarioJPA.getQuery());
            queryEscenarioJPA.setUsuario(entidad.getUsuario() != null ? entidad.getUsuario() : queryEscenarioJPA.getUsuario());
            managerDAO.edit(queryEscenarioJPA);
            return Response.status(Response.Status.OK).entity(queryEscenarioJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      }
    
     /**
     * Borra una politica por su Id
     * @param id Identificador de la entidad
     * @return El resultado de la operacion en codigo HTTP
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id) {
        QueryEscenario hijo = managerDAO.find(id);
        Escenario entidadPadreJPA = null;
        if (hijo.getEscenario() != null) {
            entidadPadreJPA = padreDAO.find(hijo.getEscenario().getId());
            entidadPadreJPA.removeIndicador(hijo);
        }
        managerDAO.remove(hijo);
        if (entidadPadreJPA != null) {
            padreDAO.edit(entidadPadreJPA);
        }
        WrapperResponseEntity mensaje = new WrapperResponseEntity(Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase(), "Registro borrado exitosamente");
        return Response.status(Response.Status.OK).entity(mensaje).build();
    }
       
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }
}
