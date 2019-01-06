/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.QueryAprobacionDAO;
import co.com.claro.model.dto.QueryAprobacionDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.QueryAprobacion;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;

import java.time.Instant;
import java.util.ArrayList;
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
@Path("queryaprobacion")
public class QueryAprobacionREST {
    @Transient
    private static final Logger logger = Logger.getLogger(QueryAprobacionREST.class.getSimpleName());
   
    private String modulo = "QUERYAPROBACION";
    
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;
    @EJB
    protected QueryAprobacionDAO managerDAO;

    @EJB
    protected ConciliacionDAO padreDAO;
    
    @GET
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<QueryAprobacionDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby,
            @QueryParam("texto") String texto) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}texto:{3}", new Object[]{offset, limit, orderby, texto});
        List<QueryAprobacionDTO> lstDTO;
        List<QueryAprobacion> lst;
        if (texto != null && !texto.isEmpty()) {
            lst = managerDAO.findByAnyColumn(texto);
        } else {
            lst = managerDAO.findRange(new int[]{offset, limit});
        }
        lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(QueryAprobacionDTO::getId)).collect(toList());
        //UtilListas.ordenarQueryEjecucion(lstDTO, orderby);
        List<QueryAprobacionDTO> lstFinal = (List<QueryAprobacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }   
    
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public QueryAprobacionDTO findById(@PathParam("id") Integer id){
        logger.log(Level.INFO, "id:{0}", id);
        QueryAprobacion entidad = managerDAO.find(id);
        return entidad.toDTO();

    }


    @GET
    @Path("/findByAny")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<QueryAprobacionDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);        
        List<QueryAprobacion> lst = managerDAO.findByAnyColumn(texto);
        List<QueryAprobacionDTO> lstDTO = new ArrayList<>();        
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<QueryAprobacionDTO> lstFinal = (List<QueryAprobacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }
   
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(QueryAprobacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Conciliacion entidadPadreJPA;
        QueryAprobacion entidadJPA = entidad.toEntity();
        entidadJPA.setConciliacion(null);
        if ( entidad.getIdConciliacion() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdConciliacion());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException("Datos no encontrados " + entidad.getIdConciliacion());
            } else {
                managerDAO.create(entidadJPA);
                entidadJPA.setConciliacion(entidadPadreJPA);
                managerDAO.edit(entidadJPA);
                entidadPadreJPA.addQueryAprobacion(entidadJPA);
                padreDAO.edit(entidadPadreJPA);
            }
        } else {
            managerDAO.create(entidadJPA);
        }
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
        logAuditoriaDAO.create(logAud);
        return Response.status(Response.Status.CREATED).entity(entidadJPA.toDTO()).build();
    }
    
    @PUT
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(QueryAprobacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        Conciliacion entidadPadreJPA = null;
        if (entidad.getIdConciliacion() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdConciliacion());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidad.getIdConciliacion());
            }
        }
        //Hallar La entidad actual para actualizarla
        QueryAprobacion entidadJPA = managerDAO.find(entidad.getId());
        if (entidadJPA != null) {
            entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
            entidadJPA.setEstadoAprobacion(entidad.getEstadoAprobacion() != null ? entidad.getEstadoAprobacion() : entidadJPA.getEstadoAprobacion());
            entidadJPA.setConciliacion(entidad.getIdConciliacion() != null ?  (entidadPadreJPA != null ? entidadPadreJPA : null) : entidadJPA.getConciliacion());
            managerDAO.edit(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud);
            return Response.status(Response.Status.OK).entity(entidadJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      }
    
       
    @GET
    @Path("/count")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }

    
}
