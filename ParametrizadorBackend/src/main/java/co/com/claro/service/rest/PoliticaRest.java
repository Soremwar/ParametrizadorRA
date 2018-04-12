package co.com.claro.service.rest;

import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.entity.Politica;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ejb.EJB;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Clase que maneja el API Rest de Politicas
 * @author Andres Bedoya
 */
@Path("politicas")
public class PoliticaRest {
    @Transient
    private static final Logger logger = Logger.getLogger(PoliticaRest.class.getSimpleName());

    @EJB
    protected PoliticaDAO managerDAO;

    /**
     * Obtiene las Politicas Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de politicas que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<PoliticaDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
        List<Politica> lst = managerDAO.findRange(new int[]{offset, limit});
        List<PadreDTO> lstDTO = new ArrayList<>();
        
        for(Politica entidad : lst) {
            lstDTO.add(entidad.toDTO());
        }
        lstDTO = UtilListas.ordenarLista(lstDTO, orderby);
        List<PoliticaDTO> variable = (List<PoliticaDTO>)(List<?>) lstDTO;
        return variable;
    }
    
    /**
     * Obtiene una Politica por id
     * @param id Identificador de politica
     * @return Una Politica que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public PoliticaDTO getById(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}", id);
        Politica entidad = managerDAO.find(id);
        return entidad.toDTO();

    }

     /**
     * Busca las politicas por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Politicas que cumplen con el criterio
     */
    @GET
    @Path("/findByAnyColum")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);      
        List<Politica> lst = managerDAO.findByAnyColumn(texto);
        return lst;
    }
   
    
    /**
     * Crea una nueva politica
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(Politica entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);    
        entidad.setFechaCreacion(Date.from(Instant.now()));
        managerDAO.create(entidad);
        return Response.status(Response.Status.CREATED).entity(entidad).build();
    }
    
    /**
     * Actualiza una politica por su Id
     * @param entidad
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(Politica entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        PoliticaDTO entidadActual = getById(entidad.getCodigo());
        if (entidadActual != null) {
            entidad.setFechaCreacion(entidadActual.getFechaCreacion());
            entidad.setFechaActualizacion(Date.from(Instant.now()));
            managerDAO.edit(entidad);
            return Response.status(Response.Status.OK).entity(entidad).build();
            
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
    public Response remove(@PathParam("id") Integer id) {
        managerDAO.remove(managerDAO.find(id));
        return Response.status(Response.Status.OK).build();
    }
    
    /**
     * Busca las politicas por una columna de texto especifica
     * @param nombre Nombre de Politica
     * @param descripcion Descripcion de Politica
     * @param objetivo Objetivo de Politica
     * @return Lista de todas las politicas que cumplen con el criterio
     */
    @GET
    @Path("/findBySpecificColum")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findBySpecificColumn(
            @DefaultValue("") @QueryParam("nombre") String nombre,
            @DefaultValue("") @QueryParam("descripcion") String descripcion,
            @DefaultValue("") @QueryParam("objetivo") String objetivo
    ){
        List<Politica> lst = managerDAO.findByColumn(nombre, descripcion, objetivo);
        return lst;
    }
    
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }
 
}
