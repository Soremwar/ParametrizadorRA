package co.com.claro.service.rest;


import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.RolDAO;
import co.com.claro.ejb.dao.UsuarioDAO;
import co.com.claro.model.dto.UsuarioDTO;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Rol;
import co.com.claro.model.entity.Rol_;
import co.com.claro.model.entity.Usuario;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import java.time.Instant;
import static java.util.Comparator.comparing;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@Path("usuarios")
public class UsuarioREST {
    @Transient
    private static final Logger logger = Logger.getLogger(UsuarioREST.class.getSimpleName());
    
    private String usuario = "admin";
    private String modulo = "politicas";
    
    @EJB
    protected UsuarioDAO managerDAO;
    
   @EJB
    protected RolDAO rolDAO;
    
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<UsuarioDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
        List<Usuario> lst = managerDAO.findRange(new int[]{offset, limit});
        List<UsuarioDTO> lstDTO = lst.stream().map(item -> (item.toDTO())).distinct().sorted(comparing(UsuarioDTO::getId).reversed()).collect(toList());
        //UtilListas.ordenarLista(lstDTO, orderby);
        List<UsuarioDTO> lstFinal = (List<UsuarioDTO>)(List<?>) lstDTO;
        return lstFinal;
    }   
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public UsuarioDTO findById(@PathParam("id") Integer id){
        logger.log(Level.INFO, "id:{0}", id);
        Usuario entidad = managerDAO.find(id);
        return entidad.toDTO();

    }
    
   
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(UsuarioDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);
        //Registrando log
        Usuario entidadJPA = dto.toEntity();
        managerDAO.create(entidadJPA);
        //LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), usuario, entidadJPA.toString());
        //logAuditoriaDAO.create(logAud);
        return Response.status(Response.Status.CREATED).entity(entidadJPA.toDTO()).build();
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(UsuarioDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);  
        //Hallar La dto actual para actualizarla
        Usuario entidadJPA = managerDAO.find(dto.getId());
        if (entidadJPA != null) {
            entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
            entidadJPA.setUsuario(dto.getUsuario() != null ? dto.getUsuario() : entidadJPA.getUsuario());
            entidadJPA.setNombreUsuario(dto.getNombreUsuario() != null ? dto.getNombreUsuario() : entidadJPA.getNombreUsuario());
            entidadJPA.setEmail(dto.getEmail() != null ? dto.getEmail() : entidadJPA.getEmail());
            
            Set<Rol> roles = new HashSet<>();
            //roles.add(new Rol)
            
            Rol rol = rolDAO.findByNombre(dto.getNombreRol());
            roles.add(rol);
            entidadJPA.setRoles(roles);
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
    public Response remove(@PathParam("id") Integer id) {
        Usuario entidadJPA = managerDAO.find(id);
        UsuarioDTO dto = entidadJPA.toDTO();
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
