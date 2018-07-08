package co.com.claro.service.rest;


import co.com.claro.ejb.dao.WsTransformacionDAO;
import co.com.claro.model.dto.WsTransformacionDTO;
import co.com.claro.model.entity.WsTransformacion;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.persistence.Transient;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Clase que maneja el API Rest de WsTransformaciones
 * @author Andres Bedoya
 */
@Path("wstransformacion")
public class WsTransformacionREST{
    @Transient
    private static final Logger logger = Logger.getLogger(WsTransformacionREST.class.getSimpleName());
   
    @EJB
    protected WsTransformacionDAO managerDAO;

    /**
     * Obtiene las Resultados Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de resultados que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<WsTransformacionDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2}", new Object[]{offset, limit, orderby});     
        List<WsTransformacion> lst = managerDAO.findRange(new int[]{offset, limit});
        List<WsTransformacionDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().collect(toList());
        //UtilListas.ordenarLista(lstDTO, orderby);
        List<WsTransformacionDTO> lstFinal = (List<WsTransformacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }   
    
    /**
     * Retorna la entidad x id
     * @param id identificador de la entidad
     * @return Retorna la entidad x id
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public WsTransformacionDTO getById(@PathParam("id") Integer id){
        logger.log(Level.INFO, "id:{0}", id);
        WsTransformacion entidad = managerDAO.find(id);
        return entidad.toDTO();

    }

     /**
     * Busca los resultados por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de WsTransformaciones que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<WsTransformacionDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);        
        List<WsTransformacion> lst = managerDAO.findByAnyColumn(texto);
        List<WsTransformacionDTO> lstDTO = new ArrayList<>();        
        for(WsTransformacion entidad : lst) {
            lstDTO.add(entidad.toDTO());
        }
        List<WsTransformacionDTO> lstFinal = (List<WsTransformacionDTO>)(List<?>) lstDTO;
        return lstFinal;
    }
   
    /**
     * Retorna el numero de registros 
     * @return numero de registros total
     */
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }
 
}
