/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.parent;

import co.com.claro.model.dto.PoliticaDTO;
import java.util.List;
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
 * @param <T>
 */
public abstract class AbstractParentREST<T> {

    /**
     *
     * @param offset
     * @param limit
     * @param orderby
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public abstract List<T> find(
        @QueryParam("offset") int offset,
        @QueryParam("limit") int limit,
        @QueryParam("orderby") String orderby);
    
    @GET
    @Path("/findById")
    @Produces({MediaType.APPLICATION_JSON})
    public abstract T findById(@PathParam("id") Integer id);

    /**
     * Busca las politicas por cualquier columna
     *
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Politicas que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public abstract List<PoliticaDTO> findByAnyColumn(@QueryParam("texto") String texto);

    /**
     * Crea una nueva politica
     *
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public abstract Response add(PoliticaDTO entidad);

    /**
     * Actualiza una politica por su Id
     *
     * @param entidad
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public abstract Response update(PoliticaDTO entidad);

    /**
     * Borra una politica por su Id
     *
     * @param id Identificador de la entidad
     * @return El resultado de la operacion en codigo HTTP
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public abstract Response remove(@PathParam("id") Integer id);

    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public abstract int count();

}
