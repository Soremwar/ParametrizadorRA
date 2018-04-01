package co.com.claro.service.rest;

import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.entity.Politica;
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
     * @return Toda la lista de politicas que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findPoliticas(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
        List<Politica> lst = managerDAO.findRange(new int[]{offset, limit});
        return lst;
    }

    /**
     * Obtiene una Politica por id
     * @param id Identificador de politica
     * @return Una Politica que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Politica getPoliticaById(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}", id);
        Politica item = managerDAO.find(id);
        return item;

    }

     /**
     * Busca las politicas por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Politicas que cumplen con el criterio
     */
    @GET
    @Path("/findByAnyColum")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findPoliticasByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);      
        List<Politica> lst = managerDAO.findByAnyColumn(texto);
        return lst;
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
    public List<Politica> findPoliticasBySpecificColumn(
            @DefaultValue("") @QueryParam("nombre") String nombre,
            @DefaultValue("") @QueryParam("descripcion") String descripcion,
            @DefaultValue("") @QueryParam("objetivo") String objetivo
    ){
        List<Politica> lst = managerDAO.findByColumn(nombre, descripcion, objetivo);
        return lst;
    }

    
    /**
     * Crea una nueva politica
     * @param entidadDTO
     * @return el DTO recien creado
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addPolitica(PoliticaDTO entidadDTO) {
        logger.log(Level.INFO, "entidadDTO:{0}", entidadDTO);    
        Politica entity = new Politica();
        entity.setNombrePolitica(entidadDTO.getNombrePolitica());
        entity.setDescripcion(entidadDTO.getDescripcion());
        entity.setObjetivo(entidadDTO.getObjetivo());
        entity.setUsuario(entidadDTO.getUsuario());
       
        managerDAO.create(entity);
        entidadDTO.setCodPolitica(entity.getCodPolitica());
        entidadDTO.setFechaCreacion(entity.getFechaCreacion());
        return Response.status(Response.Status.CREATED).entity(entidadDTO).build();
    }
    
    /**
     * Actualiza una politica por su Id
     * @param entidadDTO
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updatePolitica(PoliticaDTO entidadDTO) {
        logger.log(Level.INFO, "entidadDTO:{0}", entidadDTO);  
        if (getPoliticaById(entidadDTO.getCodPolitica()) != null) {
            Politica entity = new Politica();
            entity.setCodPolitica(entidadDTO.getCodPolitica());
            entity.setNombrePolitica(entidadDTO.getNombrePolitica());
            entity.setDescripcion(entidadDTO.getDescripcion());
            entity.setObjetivo(entidadDTO.getObjetivo());
            entity.setUsuario(entidadDTO.getUsuario());
            entity.setFechaActualizacion(entidadDTO.getFechaActualizacion());
            managerDAO.edit(entity);
        }
        return Response.status(Response.Status.OK).build();
    }
    
     /**
     * Borra una politica por su Id
     * @param entidadDTO
     * @return el resultado de la operacion
     */
    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        managerDAO.remove(getPoliticaById(id));
        return Response.status(Response.Status.OK).build();
    }

    
    
    
}
