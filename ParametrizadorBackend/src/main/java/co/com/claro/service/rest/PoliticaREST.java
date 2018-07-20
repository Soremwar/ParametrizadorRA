package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.excepciones.Mensaje;
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
public class PoliticaREST {
    @Transient
    private static final Logger logger = Logger.getLogger(PoliticaREST.class.getSimpleName());
    
    @EJB
    protected PoliticaDAO managerDAO;
    
    @EJB
    protected ConciliacionDAO conciliacionDAO;

    /**
     * Obtiene las Politicas Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de politicas que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
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
    public PoliticaDTO getById(@PathParam("id") Integer id){
        logger.log(Level.INFO, "id:{0}", id);
        Politica entidad = managerDAO.findByAllTreeById(id);
        return entidad.toDTO();

    }

    /**
     * Encontrar Politicas que no tengan conciliacion
     * @return
     */
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
     /**
     * Busca las politicas por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Politicas que cumplen con el criterio
     */
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
   
    /**
     * Crea una nueva politica
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(PoliticaDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Politica entidadAux = entidad.toEntity();
        managerDAO.create(entidadAux);
        return Response.status(Response.Status.CREATED).entity(entidadAux.toDTO()).build();
    }
    
    /**
     * Actualiza una politica por su Id
     * @param entidad
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(PoliticaDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        //Hallar La entidad actual para actualizarla
        Politica politicaJPA = managerDAO.find(entidad.getId());
        if (politicaJPA != null) {
            politicaJPA.setFechaActualizacion(Date.from(Instant.now()));
            politicaJPA.setNombre(entidad.getNombre() != null ? entidad.getNombre() : politicaJPA.getNombre());
            politicaJPA.setDescripcion(entidad.getDescripcion() != null ? entidad.getDescripcion() : politicaJPA.getDescripcion());
            politicaJPA.setObjetivo(entidad.getObjetivo() != null ? entidad.getObjetivo() : politicaJPA.getObjetivo());
            politicaJPA.setUsuario(entidad.getUsuario() != null ? entidad.getUsuario() : politicaJPA.getUsuario());
            managerDAO.edit(politicaJPA);
            return Response.status(Response.Status.OK).entity(politicaJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      }
    
     /**
     * Borra una politica por su Id
     * @param id Identificador de la entidad
     * @return El resultado de la operacion en codigo HTTP
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id) {
        managerDAO.remove(managerDAO.find(id));
        Mensaje mensaje = new Mensaje(200, "OK", "Registro borrado exitosamente");
        return Response.status(Response.Status.OK).entity(mensaje).build();
    }
    
    /*
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public int count(){
        return super.count();
    }*/
 
}
