package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.model.dto.RequestURLDTO;
import co.com.claro.model.dto.request.LoadPlanStatusRequestDTO;
import co.com.claro.model.dto.request.StartLoadPlanRequestDTO;
import co.com.claro.model.dto.request.StopLoadPlanRequestDTO;
import com.oracle.xmlns.odi.odiinvoke.FacadeODI;
import com.oracle.xmlns.odi.odiinvoke.LoadPlanStatusType;
import com.oracle.xmlns.odi.odiinvoke.OdiStartLoadPlanType;
import com.oracle.xmlns.odi.odiinvoke.OdiStopLoadPlanType;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
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

    @EJB
    protected ParametroDAO parametrosDAO;
    
    //@EJB(mappedName="stars21_web-Model-SessionEJB")
    protected FacadeODI facadeODI = new FacadeODI();
    
    protected String wsdlLocation = getWsdlLocationODIFromDB(); // = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
    //"http://Server-ODI:8088/mockInvokeRequestSOAP11Binding?wsdl";
    
    @GET
    @Path("/getWsdlLocationODIFromDB")
    public String getWsdlLocationODIFromDB() {
        String wsdlLocation;
        try {
            wsdlLocation = parametrosDAO.findByParametro("SISTEMA", "odiUrl");
        } catch (Exception e) {
            wsdlLocation = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
        }
        return wsdlLocation;
    }  
    
    @GET
    @Path("/getVersion")
    public String getVersion() {
        return facadeODI.getVersion(wsdlLocation);
        
    }  
    
    @POST
    @Path("/startLoadPlan")
    @Produces({MediaType.APPLICATION_JSON})
    //public OdiStartLoadPlanType startLoadPlan(StartLoadPlanRequestDTO request) {
    public OdiStartLoadPlanType startLoadPlan(StartLoadPlanRequestDTO request) {
        OdiStartLoadPlanType startLoadPlan = facadeODI.startLoadPlan(wsdlLocation, request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanName(), request.getContexto());
        return startLoadPlan;
    }   

    @POST
    @Path("/stopLoadPlan")
    @Produces({MediaType.APPLICATION_JSON})
    public OdiStopLoadPlanType stopLoadPlan(StopLoadPlanRequestDTO request) {
        return facadeODI.stopLoadPlan(wsdlLocation, request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanInstance(), request.getLoadPlanInstanceRunCount(), request.getStopLevel());
    }   
    
    @POST
    @Path("/loadPlanStatus")
    @Produces({MediaType.APPLICATION_JSON})
    public List<LoadPlanStatusType> loadPlanStatus(LoadPlanStatusRequestDTO request) {
        return facadeODI.loadPlanStatus(wsdlLocation, request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlans());
    }  


}
