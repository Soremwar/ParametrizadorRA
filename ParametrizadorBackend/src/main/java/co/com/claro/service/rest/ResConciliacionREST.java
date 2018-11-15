/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ResConciliacionDAO;
import co.com.claro.model.dto.ResConciliacionDTO;
import co.com.claro.model.entity.ResConciliacion;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("resconciliacion")
public class ResConciliacionREST {

    @Transient
    private static final Logger logger = Logger.getLogger(ResConciliacionREST.class.getSimpleName());

    @EJB
    protected ResConciliacionDAO managerDAO;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<ResConciliacionDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("estado") String estado,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});
        List<ResConciliacion> lst = managerDAO.findRange(new int[]{offset, limit});
        List<ResConciliacionDTO> lstDTO;
        if (estado != null && !estado.isEmpty()) {
            lstDTO = lst.stream().map(item -> item.toDTO()).filter(dto -> dto.getEstado() != null).filter(dto -> estado.contains(dto.getEstado().toUpperCase())).distinct().sorted(comparing(ResConciliacionDTO::getId)).collect(toList());
        } else {
            lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(ResConciliacionDTO::getId)).collect(toList());
        }
        //UtilListas.ordenarLista(lstDTO, orderby);
        List<ResConciliacionDTO> lstFinal = (List<ResConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResConciliacionDTO findById(@PathParam("id") Integer id) {
        logger.log(Level.INFO, "id:{0}", id);
        ResConciliacion entidad = managerDAO.find(id);
        return entidad.toDTO();

    }

    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ResConciliacionDTO> findByAnyColumn(@QueryParam("texto") String texto) {
        logger.log(Level.INFO, "texto:{0}", texto);
        List<ResConciliacion> lst = managerDAO.findByAnyColumn(texto);
        List<ResConciliacionDTO> lstDTO = new ArrayList<>();
        for (ResConciliacion entidad : lst) {
            lstDTO.add(entidad.toDTO());
        }
        List<ResConciliacionDTO> lstFinal = (List<ResConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Actualiza la entidad por su Id
     *
     * @param entidad conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(ResConciliacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        return Response.status(Response.Status.OK).entity(entidad).build();
        /*ResConciliacion entidadJPA = managerDAO.find(entidad.getId());
        if (entidadJPA != null) {
            entidadJPA.setIdEjecucion(entidad.getId() != null ? entidad.getId() : entidadJPA.getIdEjecucion());
            entidadJPA.setEstado(entidad.getEstado() != null ? entidad.getEstado() : entidadJPA.getEstado());
            managerDAO.edit(entidadJPA);
            return Response.status(Response.Status.OK).entity(entidadJPA.toDTO()).build();
       }
        return Response.status(Response.Status.NOT_FOUND).build();*/
    }

    /**
     * Retorna el numero de registros
     *
     * @return numero de registros total
     */
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public int count() {
        return managerDAO.count();
    }
}
