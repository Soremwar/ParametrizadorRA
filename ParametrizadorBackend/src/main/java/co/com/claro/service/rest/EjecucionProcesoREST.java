/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.IEjecucionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.model.dto.EjecucionProcesoDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.EjecucionProceso;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;

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
@Path("ejecucionproceso")
public class EjecucionProcesoREST {
    @Transient
    private static final Logger logger = Logger.getLogger(EjecucionProcesoREST.class.getSimpleName());
    
    private String modulo = "EJECUCIONPROCESO";
    
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;

    @EJB
    protected IEjecucionDAO managerDAO;
    
    @EJB
    protected ConciliacionDAO padreDAO;
    
    /**
     * Obtiene las Conciliaciones Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<EjecucionProcesoDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});
        List<EjecucionProceso> lst = managerDAO.findRange(new int[]{offset, limit});
        List<EjecucionProcesoDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(EjecucionProcesoDTO::getId).reversed()).collect(toList());
        //UtilListas.ordenarListaEjecucion(lstDTO, orderby);
        List<EjecucionProcesoDTO> lstFinal = (List<EjecucionProcesoDTO>)(List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una Conciliacion por id
     * @param id Identificador de conciliacion
     * @return Una Conciliacion que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public EjecucionProcesoDTO getById(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}" , id);
        EjecucionProceso entidad = managerDAO.find(id);
        return entidad.toDTO();
    }

    
    /**
     * Obtiene la entidad x escenario
     * @param id
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
    @Path("/findByEscenario")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<EjecucionProcesoDTO> getByEscenario(@QueryParam("codEscenario") int id) {
        logger.log(Level.INFO, "id:{0}", new Object[]{id});
        List<EjecucionProceso> lst = managerDAO.findByEscenario(id);
        List<EjecucionProcesoDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(EjecucionProcesoDTO::getId)).collect(toList());
        List<EjecucionProcesoDTO> lstFinal = (List<EjecucionProcesoDTO>)(List<?>) lstDTO;
        return lstFinal;
    } 
    
        /**
     * Obtiene la entidad x escenario
     * @param id
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
    @Path("/findByIdPlanInstance")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<EjecucionProcesoDTO> getIdPlanInstance(@QueryParam("idPlanInstance") String id) {
        logger.log(Level.INFO, "id:{0}", new Object[]{id});
        List<EjecucionProceso> lst = managerDAO.findByPlanInstance(id);
        List<EjecucionProcesoDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(EjecucionProcesoDTO::getId)).collect(toList());
        List<EjecucionProcesoDTO> lstFinal = (List<EjecucionProcesoDTO>)(List<?>) lstDTO;
        return lstFinal;
    } 
    
    
         /**
     * Crea una nueva politica
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(EjecucionProcesoDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Conciliacion entidadPadreJPA;
        EjecucionProceso entidadJPA = entidad.toEntity();
        entidadPadreJPA = padreDAO.find(entidad.getIdConciliacion());
        if ( entidadPadreJPA != null) {
                entidadJPA.setConciliacion(null);
                managerDAO.create(entidadJPA);
                entidadJPA.setConciliacion(entidadPadreJPA);
                managerDAO.edit(entidadJPA);
                entidadPadreJPA.addEjecucionProceso(entidadJPA);
                padreDAO.edit(entidadPadreJPA);
        }
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadJPA.toString());
        logAuditoriaDAO.create(logAud);
        return Response.status(Response.Status.CREATED).entity(entidadJPA.toDTO()).build();
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
    public Response update(EjecucionProcesoDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Conciliacion entidadPadreJPA = null;
        if (entidad.getIdConciliacion() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdConciliacion());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidad.getIdConciliacion());
            }
        }
        //Hallar La entidad actual para actualizarla
        EjecucionProceso entidadHijaJPA = managerDAO.find(entidad.getId());
        if (entidadHijaJPA != null) {
            entidadHijaJPA.setFechaEjecucion(Date.from(Instant.now()));
            entidadHijaJPA.setNombre(entidad.getNombre() != null ? entidad.getNombre() : entidadHijaJPA.getNombre());
            entidadHijaJPA.setConciliacion(entidad.getIdConciliacion() != null ?  (entidadPadreJPA != null ? entidadPadreJPA : null): entidadHijaJPA.getConciliacion());
            entidadHijaJPA.setCodEscenario(entidad.getIdEscenario() != null ? entidad.getIdEscenario() : entidadHijaJPA.getCodEscenario());
            entidadHijaJPA.setComponenteEjecutado(entidad.getComponenteEjecutado() != null ? entidad.getComponenteEjecutado() : entidadHijaJPA.getComponenteEjecutado());
            entidadHijaJPA.setEstadoEjecucion(entidad.getEstadoEjecucion()!= null ? entidad.getEstadoEjecucion(): entidadHijaJPA.getEstadoEjecucion());
            entidadHijaJPA.setFechaEjecucionExitosa(entidad.getFechaEjecucionExitosa() != null ? entidad.getFechaEjecucionExitosa() : entidadHijaJPA.getFechaEjecucionExitosa());
            entidadHijaJPA.setIdPlanInstance(entidad.getIdPlanInstance()!= null ? entidad.getIdPlanInstance(): entidadHijaJPA.getIdPlanInstance());
            entidadHijaJPA.setNombreConciliacion(entidad.getNombreConciliacion()!= null ? entidad.getNombreConciliacion(): entidadHijaJPA.getNombreConciliacion());
            entidadHijaJPA.setRespuesta(entidad.getRespuesta() != null ? entidad.getRespuesta() : entidadHijaJPA.getRespuesta());
            
            entidadHijaJPA.setNombreEscenario(entidad.getNombreEscenario() != null ? entidad.getNombreEscenario(): entidadHijaJPA.getNombreEscenario());
            managerDAO.edit(entidadHijaJPA);
            if ((entidadPadreJPA != null)){
                entidadPadreJPA.addEjecucionProceso(entidadHijaJPA);
                padreDAO.edit(entidadPadreJPA);
            }
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), entidad.getUsername(), entidadHijaJPA.toString());
            logAuditoriaDAO.create(logAud);
            return Response.status(Response.Status.OK).entity(entidadHijaJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      
    }
    
     /**
     * Borra una politica por su Id
     * @param id Identificador de la entidad
     * @return El resultado de la operacion en codigo HTTP
     */
    @DELETE
    @Path("{id}/{username}")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id, @PathParam("username") String username) {
        EjecucionProceso ejecucionProceso = managerDAO.find(id);
        WrapperResponseEntity mensaje;
        if (ejecucionProceso == null) {
                mensaje = new WrapperResponseEntity(404, "Not Found", "Registro no encontrado " + id);
                return Response.status(Response.Status.OK).entity(mensaje).build();    
        } else {
            managerDAO.remove(managerDAO.find(id));
            mensaje = new WrapperResponseEntity(200, "OK", "Registro borrado exitosamente");
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, ejecucionProceso.toString());
            logAuditoriaDAO.create(logAud);
        }
        return Response.status(Response.Status.OK).entity(mensaje).build();
    }
    
    
    @GET
    @Path("/count")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }    
  
}
