/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest;

import co.com.claro.ejb.dao.EjecucionDAO;
import co.com.claro.model.dto.EjecucionProcesoDTO;
import co.com.claro.model.entity.EjecucionProceso;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Transient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author andresbedoya
 */
@Stateless
@Path("ejecucionproceso")
public class EjecucionProcesoREST {
    @Transient
    private static final Logger logger = Logger.getLogger(EjecucionProcesoREST.class.getSimpleName());

    @EJB
    protected EjecucionDAO managerDAO;
    
    /**
     * Obtiene la entidad x escenario
     * @param id
     * @return Toda la lista de conciliaciones que corresponden con el criterio
     */
    @GET
    @Path("/findByEscenario")
    @Produces({MediaType.APPLICATION_JSON})
    public List<EjecucionProcesoDTO> getByEscenario(@QueryParam("codEscenario") int id) {
        logger.log(Level.INFO, "id:{0}", new Object[]{id});
        List<EjecucionProceso> lst = managerDAO.findByEscenario(id); //findRange(new int[]{offset, limit});
        List<EjecucionProcesoDTO> lstDTO = lst.stream().map(item -> item.toDTO()).distinct().sorted(comparing(EjecucionProcesoDTO::getId)).collect(toList());
        //UtilListas.ordenarLista(lstDTO, orderby);
        List<EjecucionProcesoDTO> lstFinal = (List<EjecucionProcesoDTO>)(List<?>) lstDTO;
        return lstFinal;
    }    
}
