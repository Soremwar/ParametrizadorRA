package co.com.claro.service.rest;

import co.com.claro.model.dto.request.LoadPlanStatusRequestDTO;
import co.com.claro.model.dto.request.StartLoadPlanRequestDTO;
import co.com.claro.model.dto.request.StopLoadPlanRequestDTO;
import co.com.claro.service.soap.odi.consumer.LoadPlanStatusType;
import co.com.claro.service.soap.odi.consumer.OdiStartLoadPlanType;
import co.com.claro.service.soap.odi.consumer.OdiStopLoadPlanType;
import co.com.claro.service.soap.odi.producer.WebServiceOdi;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.persistence.Transient;
import javax.ws.rs.POST;

/**
 * Clase que maneja el API Rest de Resultados
 * @author Andres Bedoya
 */
@Path("odiRest")
public class OdiREST{
    @Transient
    private static final Logger logger = Logger.getLogger(OdiREST.class.getSimpleName());

    //@EJB(mappedName="stars21_web-Model-SessionEJB")
    protected WebServiceOdi webServiceOdi = new WebServiceOdi();
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public String getVersion() {
        return webServiceOdi.getVersion();
    }   

    @POST
    @Path("/startLoadPlan")
    @Produces({MediaType.APPLICATION_XML})
    //public OdiStartLoadPlanType startLoadPlan(StartLoadPlanRequestDTO request) {
    public OdiStartLoadPlanType startLoadPlan(StartLoadPlanRequestDTO request) {
        OdiStartLoadPlanType startLoadPlan = webServiceOdi.startLoadPlan(request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanName(), request.getContexto());
        return startLoadPlan;
    }   

    @POST
    @Path("/stopLoadPlan")
    @Produces({MediaType.APPLICATION_XML})
    public OdiStopLoadPlanType stopLoadPlan(StopLoadPlanRequestDTO request) {
        return webServiceOdi.stopLoadPlan(request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanInstance(), request.getLoadPlanInstanceRunCount(), request.getStopLevel());
    }   
    
    @POST
    @Path("/loadPlanStatus")
    @Produces({MediaType.APPLICATION_XML})
    public List<LoadPlanStatusType> loadPlanStatus(LoadPlanStatusRequestDTO request) {
        return webServiceOdi.loadPlanStatus(request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlans());
    }  


}
