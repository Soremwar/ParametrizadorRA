package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.excepciones.MensajeError;
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
 * Clase que maneja el API Rest de Conciliaciones
 * @author Andres Bedoya
 */
@Path("conciliaciones")
public class ConciliacionRest{
    @Transient
    private static final Logger logger = Logger.getLogger(ConciliacionRest.class.getSimpleName());

    @EJB
    protected ConciliacionDAO managerDAO;
    @EJB
    protected PoliticaDAO padreDAO;
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
        //List<Conciliacion> lst = managerDAO.findByAllTree(new int[]{offset, limit});
        List<Conciliacion> lst = managerDAO.findRange(new int[]{offset, limit});
        List<PadreDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(ConciliacionDTO::getId)).collect(toList());
        //List<PadreDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().collect(toList());
        //lstDTO = lstDTO.stream().distinct().collect(toList());
        //lstDTO.sort(comparator);
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
        for(Conciliacion entidad : lst) {
            lstDTO.add(entidad.toDTO());
        }
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>)(List<?>) lstDTO;
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
    public Response add(ConciliacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Politica entidadPadreJPA = null;
        if (entidad.getIdPolitica() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdPolitica());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException("No se encontraron datos asociados a ... " + entidad.getIdPolitica());
            }
        }
        Conciliacion entidadHijaJPA = entidad.toEntity();
        entidadHijaJPA.setFechaCreacion(Date.from(Instant.now()));
        managerDAO.create(entidadHijaJPA);
        if (entidadPadreJPA != null && entidadPadreJPA.getConciliaciones() != null){
            entidadPadreJPA.getConciliaciones().add(entidadHijaJPA);
            padreDAO.edit(entidadPadreJPA);           
        }
        return Response.status(Response.Status.CREATED).entity(entidadHijaJPA.toDTO()).build();
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
                throw new DataNotFoundException("No se encontraron datos asociados a la entidad padre ... " + entidad.getIdPolitica());
            }
        }
        Conciliacion entidadHijaJPA = entidad.toEntity();
        Politica polAux = null;
        if (entidad.getIdPolitica() != null && isEntidadPadreAsignada(entidad)) {
            MensajeError mensaje = new MensajeError(500, "ERROR", "No es posible cambiar la entidad padre. Revise la peticion");
            return Response.status(Response.Status.OK).entity(mensaje).build();
        }
        ConciliacionDTO conciliacionActual = getById(entidad.getId());
        if (conciliacionActual != null) {
            entidadHijaJPA.setFechaCreacion(conciliacionActual.getFechaCreacion());
            entidadHijaJPA.setFechaActualizacion(Date.from(Instant.now())); 
            if (entidad.getIdPolitica() != null) {
                polAux = getPoliticaToAssign(entidad);
                entidadHijaJPA.setPolitica(polAux);
            }
            managerDAO.edit(entidadHijaJPA);
            if (entidadPadreJPA != null && entidadPadreJPA.getConciliaciones() != null){
                entidadPadreJPA.getConciliaciones().add(entidadHijaJPA);
                padreDAO.edit(entidadPadreJPA);           
            }
            return Response.status(Response.Status.OK).entity(entidadHijaJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      
    }
    
    /**
     * Obtiene la entidad padre que hay que asignar
     * @param entidadActualDTO
     * @return la entidad padre que hay que asignar
     */
    private Politica getPoliticaToAssign(ConciliacionDTO entidadActualDTO){
        ConciliacionDTO entidadInBDDTO = getById(entidadActualDTO.getId());
        Politica politica = new Politica();
        //Si no tiene padre en la base de datos
        if (entidadInBDDTO.getIdPolitica() == null) {
            politica.setId(entidadActualDTO.getIdPolitica());
        //Si ya tiene padre 
        } else {
            politica.setId(entidadInBDDTO.getIdPolitica());
        }    
        return politica;
    } 
    
    private Boolean isEntidadPadreAsignada(ConciliacionDTO entidadActualDTO){
        ConciliacionDTO entidadInBDDTO = getById(entidadActualDTO.getId());
        return entidadInBDDTO.getIdPolitica() != null;
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
        Conciliacion hijo = managerDAO.find(id);
        Politica entidadPadreJPA = null;
        if (hijo.getPolitica() != null) {
            entidadPadreJPA = padreDAO.find(hijo.getPolitica().getId());
            entidadPadreJPA.getConciliaciones().remove(hijo);
        }
        managerDAO.remove(hijo);
        if (entidadPadreJPA != null) {
            padreDAO.edit(entidadPadreJPA);
        }
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
