/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.IndicadorDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.IndicadorDTO;
import co.com.claro.model.entity.Escenario;
import co.com.claro.model.entity.Indicador;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.excepciones.Mensaje;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Transient;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andresbedoya
 */
@Stateless
@Path("indicadores")
public class IndicadorREST{
    @Transient
    private static final Logger logger = Logger.getLogger(IndicadorREST.class.getSimpleName());
    
    @EJB
    protected IndicadorDAO managerDAO;
    
    @EJB
    protected EscenarioDAO padreDAO;
   
       /**
     * Obtiene las Indicadors Paginadas
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id, nombre, fechaCreacion)
     * @return Toda la lista de escenarios que corresponden con el criterio
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<IndicadorDTO> find(
            @QueryParam("offset") int offset,
            @QueryParam("limit") int limit,
            @QueryParam("orderby") String orderby) {
        logger.log(Level.INFO, "offset:{0}limit:{1}orderby:{2} ", new Object[]{offset, limit, orderby});     
        List<Indicador> lst = managerDAO.findRange(new int[]{offset, limit});
        List<IndicadorDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(IndicadorDTO::getId)).collect(toList());

        lstDTO = UtilListas.ordenarListaIndicadores(lstDTO, orderby);
        List<IndicadorDTO> lstFinal = (List<IndicadorDTO>)(List<?>) lstDTO;
        return lstFinal;
    }
    
    /**
     * Obtiene un indicador por id
     * @param id Identificador de conciliacion
     * @return Una Escenario que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public IndicadorDTO getById(@PathParam("id") int id){
        logger.log(Level.INFO, "id:{0}", id);
        Indicador entidad = managerDAO.find(id);
        return entidad.toDTO();

    }
    
    /**
     * Busca los indicadores por cualquier columna
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Escenarios que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<IndicadorDTO> findByAnyColumn(@QueryParam("texto") String texto){
        logger.log(Level.INFO, "texto:{0}", texto);      
        List<Indicador> lst = managerDAO.findByAnyColumn(texto);
        List<IndicadorDTO> lstDTO = lst.stream().map(item -> item.toDTO()).sorted(comparing(IndicadorDTO::getId)).collect(toList());
        List<IndicadorDTO> lstFinal = (List<IndicadorDTO>)(List<?>) lstDTO;
        return lstFinal;        
    }

    /**
     * Crea una nueva entidad
     * @param entidad Entidad que se va a agregar
     * @return el la entidad recien creada
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response add(IndicadorDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Escenario entidadPadreJPA;
        Indicador entidadHijaJPA = entidad.toEntity();
        entidadHijaJPA.setEscenario(null);
        if ( entidad.getIdEscenario() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdEscenario());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException("Datos no encontrados " + entidad.getIdEscenario());
            } else {
                managerDAO.create(entidadHijaJPA);
                entidadHijaJPA.setEscenario(entidadPadreJPA);
                managerDAO.edit(entidadHijaJPA);
                entidadPadreJPA.addIndicador(entidadHijaJPA);
                padreDAO.edit(entidadPadreJPA);
            }
        } else {
            managerDAO.create(entidadHijaJPA);
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
    public Response update(IndicadorDTO entidad) {
        logger.log(Level.INFO, "entidad:{0}", entidad);
        Escenario entidadPadreJPA = null;
        if (entidad.getIdEscenario() != null) {
            entidadPadreJPA = padreDAO.find(entidad.getIdEscenario());
            if (entidadPadreJPA == null) {
                throw new DataNotFoundException(Response.Status.NOT_FOUND.getReasonPhrase() + entidad.getIdEscenario());
            }
        }
        //Hallar La entidad actual para actualizarla
        Indicador entidadHijaJPA = managerDAO.find(entidad.getId());
        if (entidadHijaJPA != null) {
            entidadHijaJPA.setNombreFormula(entidad.getNombreFormula()!= null ? entidad.getNombreFormula(): entidadHijaJPA.getNombreFormula());
            entidadHijaJPA.setDescripcion(entidad.getDescripcion() != null ? entidad.getDescripcion(): entidadHijaJPA.getDescripcion());
            entidadHijaJPA.setTextoFormula(entidad.getTextoFormula() != null ? entidad.getTextoFormula(): entidadHijaJPA.getTextoFormula());
            entidadHijaJPA.setEscenario(entidad.getIdEscenario() != null ?  (entidadPadreJPA != null ? entidadPadreJPA : null): entidadHijaJPA.getEscenario());
            managerDAO.edit(entidadHijaJPA);
            if ((entidadPadreJPA != null)){
                entidadPadreJPA.addIndicador(entidadHijaJPA);
                padreDAO.edit(entidadPadreJPA);
            }
            return Response.status(Response.Status.OK).entity(entidadHijaJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
      
    }
    
    /**
     * Borra una entidad por su Id
     * @param id Identificador de la identidad
     * @return el resultado de la operacion
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response remove(@PathParam("id") Integer id) {
        Indicador hijo = managerDAO.find(id);
        Escenario entidadPadreJPA = null;
        if (hijo.getEscenario() != null) {
            entidadPadreJPA = padreDAO.find(hijo.getEscenario().getId());
            entidadPadreJPA.removeIndicador(hijo);
        }
        managerDAO.remove(hijo);
        if (entidadPadreJPA != null) {
            padreDAO.edit(entidadPadreJPA);
        }
        Mensaje mensaje = new Mensaje(Response.Status.OK.getStatusCode(), Response.Status.OK.getReasonPhrase(), "Registro borrado exitosamente");
        return Response.status(Response.Status.OK).entity(mensaje).build();
    }
    
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public int count(){
        return managerDAO.count();
    }
}
