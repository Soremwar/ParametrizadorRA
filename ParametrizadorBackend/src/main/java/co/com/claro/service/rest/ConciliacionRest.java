package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.excepciones.MensajeError;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
 * Clase que maneja el API Rest de Conciliaciones
 * @author Andres Bedoya
 */
@Path("conciliaciones")
public class ConciliacionRest {
    @Transient
    private static final Logger logger = Logger.getLogger(ConciliacionRest.class.getSimpleName());

    @EJB
    protected ConciliacionDAO managerDAO;
    @EJB
    protected PoliticaDAO politicaDAO;

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
        List<PadreDTO> lstDTO = new ArrayList<>();
        for(Conciliacion entidad : lst) {
            lstDTO.add(entidad.toDTO());
        }
        lstDTO = UtilListas.ordenarLista(lstDTO, orderby);
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
        logger.log(Level.INFO, "id:{0}", id);
        Conciliacion entidad = managerDAO.find(id);
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
     * Encuentra las conciliaciaciones que no tienen asociada ninguna politica
     * @return Listado con las conciliaciones
     */   
    @GET
    @Path("/findByPoliticaNull")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> findByPoliticaNull(){   
        List<Conciliacion> lst = managerDAO.findByPoliticaNull();
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
       
        //PoliticaDTO politica = entidad.getPolitica();
        Optional<PoliticaDTO> politica = Optional.ofNullable(entidad.getPolitica());
        if(politica.isPresent()) {
            Politica politicaAux = politicaDAO.find(entidad.getPolitica().getId());
            if (politicaAux == null) {
                MensajeError mensaje = new MensajeError(404, "Politica no existe", "Esta politica no existe");
                return Response.status(Response.Status.NOT_FOUND).entity(mensaje).build();
            }
        }
        
        //PoliticaDTO politica = getById(entidad.getPolitica().getId());
        Conciliacion entidadAux = entidad.toEntity();
        entidadAux.setFechaCreacion(Date.from(Instant.now()));
        managerDAO.create(entidadAux);
        return Response.status(Response.Status.CREATED).entity(entidadAux).build();
    }   
    /**
     * Actualiza una conciliacion por su Id
     * @param entidad conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(ConciliacionDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        ConciliacionDTO entidadActual = getById(entidad.getId());
        Conciliacion entidadAux = entidad.toEntity();
        if (getById(entidad.getId()) != null) {
            entidadAux.setFechaCreacion(entidadActual.getFechaCreacion());
            entidadAux.setFechaActualizacion(Date.from(Instant.now()));
            if (entidadActual.getPolitica() == null) {
                entidadAux.setPolitica(entidad.getPolitica().toEntity());
            } else {
                entidadAux.setPolitica(entidadActual.getPolitica().toEntity());
            }
            managerDAO.edit(entidadAux);
            return Response.status(Response.Status.OK).entity(entidadAux.toDTO()).build();
            
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
     /**
     * Borra una conciliacion por su Id
     * @param id Identificador de la identidad
     * @param entidadDTO
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
