package co.com.claro.service.rest;


import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.RolDAO;
import co.com.claro.ejb.dao.UsuarioDAO;
import co.com.claro.ejb.dao.UsuarioRolDAO;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.CredencialesDTO;
import co.com.claro.model.dto.LoginDTO;
import co.com.claro.model.dto.UsuarioDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Rol;
import co.com.claro.model.entity.Usuario;
import co.com.claro.model.entity.UsuarioRol;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.ldap.AutenticacionLDAP;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.tokenFilter.JWTLogin;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeededFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Clase que maneja el API Rest de Usuarios
 * @author Andres Bedoya
 */
@Stateless
@Path("usuarios")
public class UsuarioREST {
    @Transient
    private static final Logger logger = Logger.getLogger(UsuarioREST.class.getSimpleName());
    
    private String modulo = "USUARIOS";
        
    @EJB
    protected UsuarioDAO managerDAO;
    
    @EJB
    protected UsuarioRolDAO userRolDAO;
 
   @EJB
    protected RolDAO rolDAO;
    
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;

    @GET
    @JWTTokenNeeded
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
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public UsuarioDTO findById(@PathParam("id") Integer id){
        logger.log(Level.INFO, "id:{0}", id);
        Usuario entidad = managerDAO.find(id);
        return entidad.toDTO();

    }
    
    /**
     * Busca los usuarios por cualquier columna
     *
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Usuarios que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<UsuarioDTO> findByAnyColumn(@QueryParam("texto") String texto) {
        logger.log(Level.INFO, "texto:{0}", texto);
        List<Usuario> lst = managerDAO.findByAnyColumn(texto);
        List<UsuarioDTO> lstDTO = new ArrayList<>();
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<UsuarioDTO> lstFinal = (List<UsuarioDTO>) (List<?>) lstDTO;
        return lstFinal;
    }
    
    @POST
    //@JWTLogin 
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response authenticateUser(CredencialesDTO credentials) {
        try {
        	AutenticacionLDAP auth = new AutenticacionLDAP();
        	Usuario user = new Usuario();
        	
        	//boolean isLoged = auth.login(credentials.getUserName(), credentials.getPassWord(), credentials.getIp().trim(), credentials.getPort().trim(), credentials.getCommonName().trim().replace("*", ""), credentials.getDomainGroup().trim(), credentials.getOrganization().trim().replace("*", ""));
        	boolean isLoged = true;
        	
        	
        	
        	if(isLoged) {
        		
        	 List<Usuario> entity = managerDAO.findByNombreUsuarioLogin(credentials.getUserName().trim());
        	 
        	 
        	 if (entity == null || entity.isEmpty()) {
        		 String token = issueToken(credentials.getUserName());
        		 
        		 LoginDTO response = new LoginDTO();
        		 response.setToken("Bearer " + token);
        		 
        		 
           		 return Response.ok(response).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
             }else {
            	 String token = "Bearer " + issueToken(entity.get(0).getNombreUsuario());
            	 Usuario findUser = entity.get(0);
            	 findUser.getRoles().stream().forEach(System.out::println);
            	 
            	 LoginDTO response = new LoginDTO();
            	 response.setEmail(findUser.getEmail());
            	 response.setFechaActualizacion(findUser.getFechaActualizacion());
            	 response.setId(findUser.getId());
            	 response.setNombreUsuario(findUser.getNombreUsuario());
            	 response.setRoles(findUser.getRoles());
            	 response.setToken(token);
            	 response.setUsuario(findUser.getUsuario());
            	 
           		 return Response.ok(response).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
             }
               	      	 
        	}else {
        		 return Response.status(Response.Status.UNAUTHORIZED).build();
        	}
        	                    	   
        } catch (Exception e) {
        	logger.log(Level.INFO, e.toString());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
    
    public static void main(String[] args) {
    	System.out.println(new UsuarioREST().issueToken("oscar"));
    }
    
    private String issueToken(String login) {
    	
    	//Calculamos la fecha de expiración del token--- Tiene configurado 2H
    	Date issueDate = new Date();
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(issueDate);
    	calendar.add(Calendar.MINUTE, 120);
        Date expireDate = calendar.getTime();
        
		//Creamos el token
        String jwtToken = Jwts.builder()
        		.claim("user", login)
                .setSubject(login)
                .setIssuer("parametrizadorClaro")
                .setIssuedAt(issueDate)  
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JWTTokenNeededFilter.KEY)
                .compact();
       
        return jwtToken;
    }
    
   
    @POST
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(UsuarioDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);
        //Registrando log
        Usuario entidadJPA = dto.toEntity();
        managerDAO.create(entidadJPA);
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), dto.getUsername(), entidadJPA.toString());
        logAuditoriaDAO.create(logAud);
        return Response.status(Response.Status.CREATED).entity(entidadJPA.toDTO()).build();
    }
    
    @PUT
    @JWTTokenNeeded
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(UsuarioDTO dto) {
        //Hallar La dto actual para actualizarla
        Usuario entidadJPA = managerDAO.find(dto.getId());
        if (entidadJPA != null) {
        	
        	entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
            entidadJPA.setUsuario(dto.getUsuario() != null ? dto.getUsuario() : entidadJPA.getUsuario());
            entidadJPA.setNombreUsuario(dto.getNombreUsuario() != null ? dto.getNombreUsuario() : entidadJPA.getNombreUsuario());
            entidadJPA.setEmail(dto.getEmail() != null ? dto.getEmail() : entidadJPA.getEmail());
            
            Set<Rol> roles = new HashSet<>();
        	
        	if(dto.getIdrol().equals(0)) {
        		               
                UsuarioRol roluser = userRolDAO.findUsuarioRol(dto.getId());
                if(roluser != null) {
                	userRolDAO.deleteRol(dto.getId());
                	roles.clear();
                }
        		
        	}else {
        		                 
                 Rol rol = rolDAO.find(dto.getIdrol());
                 roles.add(rol);
        	}
        	
        	entidadJPA.setRoles(roles);
                       
            managerDAO.edit(entidadJPA);
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), dto.getUsername(), entidadJPA.toString());
            logAuditoriaDAO.create(logAud);
            return Response.status(Response.Status.OK).entity(entidadJPA.toDTO()).build();
       }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
     
    @DELETE
    @JWTTokenNeeded
    @Path("{id}/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id, @PathParam("username") String username) {
        Usuario entidadJPA = managerDAO.find(id);
        UsuarioDTO dto = entidadJPA.toDTO();
        managerDAO.deleteUser(id);
        //managerDAO.remove(entidadJPA); //remueve todas las relaciones en cascada
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), username, entidadJPA.toString());
        logAuditoriaDAO.create(logAud);
        WrapperResponseEntity mensaje = new WrapperResponseEntity(Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase(), "Registro borrado exitosamente");
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
