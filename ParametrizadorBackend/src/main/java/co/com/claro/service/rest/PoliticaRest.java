package co.com.claro.service.rest;

import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.entity.Politica;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ejb.EJB;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Clase que maneja el API Rest de Politicas
 *
 * @author Andres Bedoya
 */
@Path("politicas")
public class PoliticaRest {
    @Transient
    private static final Logger logger = Logger.getLogger(PoliticaRest.class.getSimpleName());

    @EJB
    protected PoliticaDAO managerDAO;

    /**
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @return Toda la lista de politicas que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findRange(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String order) {
        List<Politica> lst = managerDAO.findRange(new int[]{offset, limit});
        //List<Politica> lst2; 
        //lst2 = lst.stream().sorted(Comparator.comparing((Politica p) -> p.getNombrePolitica()));
        return lst;
    }

    /**
     *
     * @param id Identificador de politica
     * @return
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Politica find(@PathParam("id") Integer id) {
        Politica item = managerDAO.find(id);
        return item;
    }

    /**
     *
     * @param nombre Nombre de Politica
     * @param descripcion Descripcion de Politica
     * @param objetivo Objetivo de Politica
     * @return Lista de todas las politicas que cumplen con el criterio
     */
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findByAnyColumn(
            @DefaultValue("") @QueryParam("nombre") String nombre,
            @DefaultValue("") @QueryParam("descripcion") String descripcion,
            @DefaultValue("") @QueryParam("objetivo") String objetivo
    ) {
        List<Politica> lst = managerDAO.findByColumn(nombre, descripcion, objetivo);
        return lst;
    }

    
    /**
     *
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Politicas que cumplen con el criterio
     */
    @GET
    @Path("/queryAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findByAnyColumn(@QueryParam("texto") String texto) {
        List<Politica> lst = managerDAO.findByAnyColumn(texto);
        return lst;
    }

    
    /**
     * 
     * @param entidadDTO
     * @param entity
     * @return 
     */
    /*@POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(Politica entity) {
        managerDAO.create(entity);
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }*/
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(PoliticaDTO entidadDTO) {
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
}
