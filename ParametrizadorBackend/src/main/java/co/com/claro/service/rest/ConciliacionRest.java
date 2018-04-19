package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
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
 * Clase que maneja el API Rest de Conciliaciones
 * @author Andres Bedoya
 */
@Path("conciliaciones")
public class ConciliacionRest {
    @Transient
    private static final Logger logger = Logger.getLogger(ConciliacionRest.class.getSimpleName());

    @EJB
    protected ConciliacionDAO managerDAO;

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
        List<ConciliacionDTO> variable = (List<ConciliacionDTO>)(List<?>) lstDTO;
        return variable;
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
    @Path("/findByAnyColum")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conciliacion> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);      
        List<Conciliacion> lst = managerDAO.findByAnyColumn(texto);
        return lst;
    }
   
    /**
     * Crea una nueva conciliacion
     * @param entidad
     * @return el DTO recien creado
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(Conciliacion entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        entidad.setFechaCreacion(Date.from(Instant.now()));
        managerDAO.create(entidad);
        return Response.status(Response.Status.CREATED).entity(entidad).build();
        
        //NO FUNCIONA
        /*logger.log(Level.INFO, "entidadDTO:{0}", entidad);  
  
        Conciliacion conciliacion = new Conciliacion();
        conciliacion.setNombreConciliacion(entidad.getNombreConciliacion());
        conciliacion.setDescripcion(entidad.getDescripcion());
        conciliacion.setUsuario(entidad.getUsuario());
        conciliacion.setTablaDestino(entidad.getTablaDestino());
        conciliacion.setCamposTablaDestino(entidad.getCamposTablaDestino());
        conciliacion.setFechaActualizacion(Date.from(Instant.now()));
        conciliacion.setFechaCreacion(Date.from(Instant.now()));
        
        Collection<Conciliacion> tblConciliacionCollection = new ArrayList<Conciliacion>();
        Politica politica = new Politica();
        politica.setCodPolitica(entidad.getCodPolitica());
        tblConciliacionCollection.add(conciliacion);
        politica.setTblConciliacionCollection(tblConciliacionCollection);
        
        conciliacion.setPolitica(politica);
        
        managerDAO.create(conciliacion);
        //entidadDTO.setCodConciliacion(entidad.getCodConciliacion());
        //entidadDTO.setFechaCreacion(entidad.getFechaCreacion());
        return Response.status(Response.Status.CREATED).entidad(conciliacion).build();
        */
    }
    
    
    /**
     * Actualiza una conciliacion por su Id
     * @param entidad conciliacion con la cual se va a trabajar
     * @return el resultado de la operacion
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(Conciliacion entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);  
        ConciliacionDTO entidadActual = getById(entidad.getCodigo()); 
        if (getById(entidad.getCodigo()) != null) {
            entidad.setFechaCreacion(entidadActual.getFechaCreacion());
            entidad.setFechaActualizacion(Date.from(Instant.now()));
            managerDAO.edit(entidad);
            return Response.status(Response.Status.OK).entity(entidad).build();
            
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
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }
}
