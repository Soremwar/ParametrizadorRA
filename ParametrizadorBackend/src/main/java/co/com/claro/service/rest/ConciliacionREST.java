package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ConciliacionDAO;
import co.com.claro.ejb.dao.EscenarioDAO;
import co.com.claro.ejb.dao.LogAuditoriaDAO;
import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.ejb.dao.WsTransformacionDAO;
import co.com.claro.ejb.dao.utils.UtilListas;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.model.entity.Politica;
import co.com.claro.model.entity.WsTransformacion;
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
 *
 * @author Andres Bedoya
 */
@Path("conciliaciones")
public class ConciliacionREST {

    @Transient
    private static final Logger logger = Logger.getLogger(ConciliacionREST.class.getSimpleName());

    private String usuario = "admin";
    private String modulo = "conciliaciones";

    @EJB
    protected LogAuditoriaDAO logAuditoriaDAO;

    @EJB
    protected PoliticaDAO padreDAO;

    @EJB
    protected ConciliacionDAO managerDAO;

    @EJB
    protected EscenarioDAO escenarioDAO;

    @EJB
    protected WsTransformacionDAO transformacionDAO;

    /**
     * Obtiene las Conciliaciones Paginadas
     *
     * @param offset Desde cual item se retorna
     * @param limit Limite de items a retornar
     * @param orderby Indica por cual campo descriptivo va a guardar (id,
     * nombre, fechaCreacion)
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
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Obtiene una Conciliacion por id
     *
     * @param id Identificador de conciliacion
     * @return Una Conciliacion que coincide con el criterio
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ConciliacionDTO getById(@PathParam("id") int id) {
        logger.log(Level.INFO, "id:{0}", id);
        Conciliacion entidad = managerDAO.find(id);
        return entidad.toDTO();
    }

    /**
     * Busca las conciliaciones por cualquier columna
     *
     * @param texto Texto a buscar en cualquier texto
     * @return Lista de Conciliacions que cumplen con el criterio
     */
    @GET
    @Path("/findByAny")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> findByAnyColumn(@QueryParam("texto") String texto) {
        logger.log(Level.INFO, "texto:{0}", texto);
        List<Conciliacion> lst = managerDAO.findByAnyColumn(texto);
        List<PadreDTO> lstDTO = new ArrayList<>();
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    @GET
    @Path("/conciliacionesRequierenAprobacion")
    @Produces({MediaType.APPLICATION_JSON})
    public List<ConciliacionDTO> findConciliacionesRequierenAprobacion() {
        //logger.log(Level.INFO, "tipo:{0}codPadre:{1}", new Object[]{requiereaprobacion});
        String requiereaprobacion = "SI";
        List<Conciliacion> lst = managerDAO.findByRequiereAprobacion(requiereaprobacion);
        List<ConciliacionDTO> lstDTO = lst.stream().map(item -> item.toDTO()).sorted(comparing(ConciliacionDTO::getId)).collect(toList());
        List<ConciliacionDTO> lstFinal = (List<ConciliacionDTO>) (List<?>) lstDTO;
        return lstFinal;
    }

    /**
     * Crea una nueva politica
     *
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
        entidadPadreJPA = padreDAO.find(dto.getIdPolitica());
        transformacionDAO.verificarSiExistePaqueteWs(dto.getPaquete());
        /*if (transformacionDAO.findByPaqueteWs(dto.getPaquete()) != null) {
            //throw new DataAlreadyExistException(Response.Status.NOT_ACCEPTABLE.getReasonPhrase() +  dto.getPaquete());
            WrapperResponseEntity response = new WrapperResponseEntity(ResponseCode.CONFLICT,  "Informacion ya existe", "El paquete " + dto.getPaquete() + " ya existe");
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(response).build();
        }*/ 
        if (entidadPadreJPA != null) {
            entidadJPA.setPolitica(null);
            managerDAO.create(entidadJPA);
            entidadJPA.setPolitica(entidadPadreJPA);
            managerDAO.edit(entidadJPA);
            entidadPadreJPA.addConciliaciones(entidadJPA);
            padreDAO.edit(entidadPadreJPA);

            if (dto.getPaquete() != null) {
                WsTransformacion transformacion = new WsTransformacion();
                transformacion.setFechaCreacion(Date.from(Instant.now()));
                transformacion.setNombreWs(dto.getPaquete());
                transformacion.setPaqueteWs(dto.getPaquete());
                transformacionDAO.create(transformacion);
                transformacion.setConciliacion(entidadJPA);
                transformacionDAO.edit(transformacion);
                entidadJPA.addTransformacion(transformacion);
                managerDAO.edit(entidadJPA);
                //padreDAO.edit(entidadPadreJPA);
            }
        }
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.AGREGAR.name(), Date.from(Instant.now()), usuario, entidadJPA.toString());
        logAuditoriaDAO.create(logAud);
        return Response.status(Response.Status.CREATED).entity(entidadJPA.toDTO()).build();
    }

    /**
     * Actualiza la entidad por su Id
     *
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
            entidadJPA.setPolitica(entidad.getIdPolitica() != null ? (entidadPadreJPA != null ? entidadPadreJPA : null) : entidadJPA.getPolitica());
            managerDAO.edit(entidadJPA);
            if ((entidadPadreJPA != null)) {
                entidadPadreJPA.addConciliaciones(entidadJPA);
                padreDAO.edit(entidadPadreJPA);
            }
            LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.EDITAR.name(), Date.from(Instant.now()), usuario, entidadJPA.toString());
            logAuditoriaDAO.create(logAud);
            return Response.status(Response.Status.OK).entity(entidadJPA.toDTO()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }

    /**
     * Borra una conciliacion por su Id
     *
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
            entidadPadreJPA.removeConciliaciones(entidadJPA);
        }
        managerDAO.remove(entidadJPA);
        LogAuditoria logAud = new LogAuditoria(this.modulo, Constantes.Acciones.BORRAR.name(), Date.from(Instant.now()), usuario, dto.toString());
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
    public int count() {
        return managerDAO.count();
    }
}
