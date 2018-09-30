package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.response.WrapperResponseEntity;
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
 * Clase que maneja el API Rest de la entidad
 * @author Andres Bedoya
 */
@Path("conciliaciones")
public class ConciliacionREST{
    @Transient
    private static final Logger logger = Logger.getLogger(ConciliacionREST.class.getSimpleName());

    private String usuario = "admin";
    private String modulo = "politica";
    
    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;
    
    @EJB
    protected PoliticaDAO padreDAO;
    
    @EJB
    protected ConciliacionDAO managerDAO;

    @EJB
    protected EscenarioDAO escenarioDAO;

    /**
     * Obtiene las Conciliaciones Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});
        List<Conciliacion> lst = managerDAO.findRange(new int[]{offset, limit});
        List<PadreDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(ConciliacionDTO::getId)).collect(toList());
        UtilListas.ordenarLista(lstDTO, orderby);
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }


    /**
     * Obtiene una Conciliacion por id
     * @param id Identificador de conciliacion
     * @return Una Conciliacion que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ConciliacionDTO getById(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}" , id);
        Conciliacion entidad = managerDAO.findByAllTreeById(id);
        return entidad.toDTO();
    }

    
    /**
     * Busca las conciliaciones por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Conciliacions que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);
        List<Conciliacion> lst = managerDAO.findByAnyColumn(texto);
        List<PadreDTO> lstDTO = new ArrayList<>();
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }

     /**
     * Crea una nueva politica
     * @param dto Entidad que se va a agregar
     * @return el la dto recien creada
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(ConciliacionDTO dto) {
        logger.log(Level.INFO, "entidad:{0}", dto);
        Politica entidadPadreJPA;
        Conciliacion entidadJPA = dto.toEntity();
        entidadJPA.setPolitica(null);
        if ( dto.getIdPolitica() != null) {
            entidadPadreJPA = padreDAO.find(dto.getIdPolitica());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException("Datos no encontrados " + dto.getIdPolitica());
            } else {
                managerDAO.create(entidadJPA);
                entidadJPA.setPolitica(entidadPadreJPA);
                managerDAO.edit(entidadJPA);
                entidadPadreJPA.addConciliacion(entidadJPA);
                padreDAO.edit(entidadPadreJPA);
            }
        } else {
            managerDAO.create(entidadJPA);
        }
        LogAuditoria logAud = new LogAuditoria(modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), usuario, entidadJPA.toString());
        logAuditoriaDAO.create(logAud);
        return Response.status(Response.Status.CREATED).entity(entidadJPA.toDTO()).build();
    }
    
    /**
     * Actualiza la entidad por su Id
     * @param entidad conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(ConciliacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Politica entidadPadreJPA = null;
        if (entidad.getIdPolitica() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdPolitica());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidad.getIdPolitica());
            }
        }
        //Hallar La entidad actual para actualizarla
        Conciliacion entidadJPA = managerDAO.find(entidad.getId());
        if (entidadJPA != null) {
            entidadJPA.setFechaActualizacion(Date.from(Instant.now()));
            entidadJPA.setNombre(entidad.getNombre() != null ? entidad.getNombre() : entidadJPA.getNombre());
            entidadJPA.setTablaDestino(entidad.getTablaDestino() != null ? entidad.getTablaDestino() : entidadJPA.getTablaDestino());
            entidadJPA.setCamposTablaDestino(entidad.getCamposTablaDestino() != null ? entidad.getCamposTablaDestino() : entidadJPA.getCamposTablaDestino());
            entidadJPA.setDescripcion(entidad.getDescripcion() != null ? entidad.getDescripcion() : entidadJPA.getDescripcion());
            entidadJPA.setUsuarioAsignado(entidad.getUsuarioAsignado() != null ? entidad.getUsuarioAsignado() : entidadJPA.getUsuarioAsignado());
            entidadJPA.setPolitica(entidad.getIdPolitica() != null ?  (entidadPadreJPA != null ? entidadPadreJPA : null): entidadJPA.getPolitica());
            managerDAO.edit(entidadJPA);
            if ((entidadPadreJPA != null)){
                entidadPadreJPA.addConciliacion(entidadJPA);
                padreDAO.edit(entidadPadreJPA);
            }
            LogAuditoria logAud = new LogAuditoria(modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), usuario, entidadJPA.toString());
            logAuditoriaDAO.create(logAud);
            return Response.status(Response.Status.OK).entity(entidadJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      
    }
    
    /**
     * Borra una conciliacion por su Id
     * @param id Identificador de la identidad
     * @return el resultado de la operacion
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id) {
        Conciliacion entidadJPA = managerDAO.find(id);
        ConciliacionDTO dto = entidadJPA.toDTO();
        Politica entidadPadreJPA = null;
        if (entidadJPA.getPolitica() != null) {
            entidadPadreJPA = padreDAO.find(entidadJPA.getPolitica().getId());
            entidadPadreJPA.removeConciliacion(entidadJPA);
        }
        managerDAO.remove(entidadJPA);
        LogAuditoria logAud = new LogAuditoria(modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), usuario, dto.toString());
        logAuditoriaDAO.create(logAud);
        if (entidadPadreJPA != null) {
            padreDAO.edit(entidadPadreJPA);
        }
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
