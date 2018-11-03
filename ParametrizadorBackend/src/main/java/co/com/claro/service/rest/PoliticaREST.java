package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.parent.AbstractParentREST;
import java.time.Instant;
import java.util.ArrayList;
import static java.util.Comparator.comparing;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ejb.EJB;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
public class PoliticaREST extends AbstractParentREST<PoliticaDTO>{
    @Transient
    private static final Logger logger = Logger.getLogger(PoliticaREST.class.getSimpleName());
    
    private String usuario = "admin";
    private String modulo = "politicas";
    
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;
    
    @EJB
    protected PoliticaDAO managerDAO;
    
    @EJB
    protected ConciliacionDAO conciliacionDAO;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public List<PoliticaDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
        List<Politica> lst = managerDAO.findRange(new int[]{offset, limit});
        List<PadreDTO> lstDTO = lst.stream().map(item -> (item.toDTO())).distinct().sorted(comparing(PadreDTO::getId)).collect(toList());
        UtilListas.ordenarLista(lstDTO, orderby);
        List<PoliticaDTO> lstFinal = (List<PoliticaDTO>)(List<?>) lstDTO;
        return lstFinal;
    }   
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public PoliticaDTO findById(@PathParam("id") Integer id){
        logger.log(Level.INFO, "id:{0}", id);
        Politica entidad = managerDAO.findByAllTreeById(id);
        return entidad.toDTO();

    }

    
    @GET
    @Path("/findPoliticasSinConciliacion")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PoliticaDTO> findPoliticasSinConciliacion(){
        List<Politica> lst = managerDAO.findPoliticaSinConciliacion();
        List<PadreDTO> lstDTO = new ArrayList<>();        
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<PoliticaDTO> lstFinal = (List<PoliticaDTO>)(List<?>) lstDTO;
        return lstFinal;
    }

    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<PoliticaDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);        
        List<Politica> lst = managerDAO.findByAnyColumn(texto);
        List<PadreDTO> lstDTO = new ArrayList<>();        
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<PoliticaDTO> lstFinal = (List<PoliticaDTO>)(List<?>) lstDTO;
        return lstFinal;
    }
   
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(PoliticaDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);
        //Registrando log
        Politica entidadJPA = dto.toEntity();
        managerDAO.create(entidadJPA);
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), usuario, entidadJPA.toString());
        logAuditoriaDAO.create(logAud);
        return Response.status(Response.Status.CREATED).entity(entidadJPA.toDTO()).build();
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(PoliticaDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);  
        //Hallar La dto actual para actualizarla
        Politica entidadJPA = managerDAO.find(dto.getId());
        if (entidadJPA != null) {
            entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
            entidadJPA.setNombre(dto.getNombre() != null ? dto.getNombre() : entidadJPA.getNombre());
            entidadJPA.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion() : entidadJPA.getDescripcion());
            entidadJPA.setObjetivo(dto.getObjetivo() != null ? dto.getObjetivo() : entidadJPA.getObjetivo());
            managerDAO.edit(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), usuario, entidadJPA.toString());
            logAuditoriaDAO.create(logAud);
            return Response.status(Response.Status.OK).entity(entidadJPA.toDTO()).build();
       }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
     /**
     * Borra una politica por su Id
     * @param id Identificador de la entidadJPA
     * @return El resultado de la operacion en codigo HTTP
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public Response remove(@PathParam("id") Integer id) {
        Politica entidadJPA = managerDAO.find(id);
        PoliticaDTO dto = entidadJPA.toDTO();
        managerDAO.remove(entidadJPA);
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), usuario, dto.toString());
        logAuditoriaDAO.create(logAud);
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
