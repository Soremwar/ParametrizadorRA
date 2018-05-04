package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.EscenarioDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.excepciones.MensajeError;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Clase que maneja el API Rest de Escenarios
 * @author Andres Bedoya
 */
@Path("escenarios")
public class EscenarioRest {
    @Transient
    private static final Logger logger = Logger.getLogger(EscenarioRest.class.getSimpleName());

    @EJB
    protected EscenarioDAO managerDAO;
    
    @EJB
    protected ConciliacionDAO conciliacionDAO;

    /**
     * Obtiene las Escenarios Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de escenarios que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<EscenarioDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
        List<Escenario> lst = managerDAO.findRange(new int[]{offset, limit});
        List<PadreDTO> lstDTO = new ArrayList<>();
        for(Escenario entidad : lst) {
            lstDTO.add(entidad.toDTO());
        }
        lstDTO = UtilListas.ordenarLista(lstDTO, orderby);
        List<EscenarioDTO> lstFinal = (List<EscenarioDTO>)(List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una Escenario por id
     * @param id Identificador de conciliacion
     * @return Una Escenario que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public EscenarioDTO getById(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}", id);
        Escenario entidad = managerDAO.find(id);
        return entidad.toDTO();

    }

     /**
     * Busca las escenarios por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Escenarios que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<EscenarioDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);      
        List<Escenario> lst = managerDAO.findByAnyColumn(texto);
        List<PadreDTO> lstDTO = new ArrayList<>();        
        for(Escenario entidad : lst) {
            lstDTO.add(entidad.toDTO());
        }
        List<EscenarioDTO> lstFinal = (List<EscenarioDTO>)(List<?>) lstDTO;
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
    public Response add(EscenarioDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        if (entidad.getIdConciliacion() != null) {
            Conciliacion conciliacionAux = conciliacionDAO.find(entidad.getIdConciliacion());
            if (conciliacionAux == null) {
                throw new DataNotFoundException("No se encontraron datos asociados a la conciliacion " + entidad.getIdConciliacion());
            }
        }
        Escenario entidadJPA = entidad.toEntity();
        entidadJPA.setFechaCreacion(Date.from(Instant.now()));
        managerDAO.create(entidadJPA);
        return Response.status(Response.Status.CREATED).entity(entidadJPA).build();
    }   
    /**
     * Actualiza una conciliacion por su Id
     * @param entidad conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(EscenarioDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        Escenario entidadJpa = entidad.toEntity();
        if (getById(entidad.getId()) != null) {
            Conciliacion conciliacionAux = conciliacionDAO.find(entidad.getIdConciliacion());
            if (conciliacionAux == null) {
                throw new DataNotFoundException("No se encontraron datos asociados a la conciliacion " + entidad.getIdConciliacion());
            }
            entidadJpa.setFechaCreacion(entidadJpa.getFechaCreacion());
            entidadJpa.setFechaActualizacion(Date.from(Instant.now()));
            entidadJpa.setConciliacion(getConciliacionToAssign(entidad));
            managerDAO.edit(entidadJpa);
            return Response.status(Response.Status.OK).entity(entidadJpa.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    
    private Conciliacion getConciliacionToAssign(EscenarioDTO entidadInDTO){
        EscenarioDTO entidadXIdDTO = getById(entidadInDTO.getId());
        Conciliacion conciliacion = new Conciliacion();
        if (entidadXIdDTO.getIdConciliacion() == null) {
            conciliacion.setId(entidadInDTO.getIdConciliacion());
        } else {
            conciliacion.setId(entidadXIdDTO.getIdConciliacion());
        }    
        return conciliacion;
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
        managerDAO.remove(managerDAO.find(id));
        MensajeError mensaje = new MensajeError(200, "OK", "Registro borrado exitosamente");
        return Response.status(Response.Status.OK).entity(mensaje).build();
    }
    
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }
}
