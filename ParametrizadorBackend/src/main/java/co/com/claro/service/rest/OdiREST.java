package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.model.dto.RequestURLDTO;
import co.com.claro.model.dto.request.LoadPlanStatusRequestDTO;
import co.com.claro.model.dto.request.StartLoadPlanRequestDTO;
import co.com.claro.model.dto.request.StopLoadPlanRequestDTO;
import co.com.claro.service.soap.odi.consumer.LoadPlanStatusType;
import co.com.claro.service.soap.odi.consumer.OdiStartLoadPlanType;
import co.com.claro.service.soap.odi.consumer.OdiStopLoadPlanType;
import co.com.claro.service.soap.odi.producer.WebServiceOdi;
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
    protected WebServiceOdi webServiceOdi = new WebServiceOdi();
    
    protected String url = "http://192.168.0.5:8088/mockInvokeRequestSOAP11Binding?wsdl";
    
    @POST
    @Path("/setURL")
    public String setURL(RequestURLDTO request) {
        url = request.getUrl();
        //"http://localhost:8088/mockInvokeRequestSOAP11Binding?WSDL";
        return url;
    }

    @GET
    @Path("/getUrlOdiActual")
    public String getUrlODI() {
        return url;
    }  
    
    /*
        @GET
    @Path("/getUrlODI")
    public String getUrlODI() {
        try {
            url = parametrosDAO.findByParametro("SISTEMA", "odiUrl");
        } catch (Exception e) {
            url = getUrlODIDefault();
        }
        return url;
    }  
    */
    
    @GET
    @Path("/getUrlODIFromDB")
    public String getUrlODIFromDB() {
        try {
            url = parametrosDAO.findByParametro("SISTEMA", "odiUrl");
        } catch (Exception e) {
            url = getUrlODIDefault();
        }
        return url;
    }  
    
    @POST
    @Path("/getVersion")
    public String getVersion(RequestURLDTO request) {
        return webServiceOdi.getVersion(request.getUrl());
        
    }
    
    /*@GET
    @Path("/getVersion")
    public String getVersion(String host, String puerto, ) {
        url = getUrlODI();
        //"http://localhost:8088/mockInvokeRequestSOAP11Binding?WSDL";//getUrlODI();
        return webServiceOdi.getVersion("http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl");
    }*/
    
    @GET
    @Path("/getVersionMock")
    public String getVersionMockup() {
        url = getUrlODI();
        //"http://localhost:8088/mockInvokeRequestSOAP11Binding?WSDL";//getUrlODI();
        return webServiceOdi.getVersion("http://192.168.0.5:9090/odiMockup?WSDL");
    }
    
    
    @GET
    @Path("/getUrlODIDefault")
    public String getUrlODIDefault() {
        return "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
    }
    
    

    
    @POST
    @Path("/startLoadPlan")
    @Produces({MediaType.APPLICATION_JSON})
    //public OdiStartLoadPlanType startLoadPlan(StartLoadPlanRequestDTO request) {
    public OdiStartLoadPlanType startLoadPlan(StartLoadPlanRequestDTO request) {
        url = getUrlODI();
        OdiStartLoadPlanType startLoadPlan = webServiceOdi.startLoadPlan(url, request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanName(), request.getContexto());
        return startLoadPlan;
    }   

    @POST
    @Path("/stopLoadPlan")
    @Produces({MediaType.APPLICATION_JSON})
    public OdiStopLoadPlanType stopLoadPlan(StopLoadPlanRequestDTO request) {
        url = getUrlODI();
        return webServiceOdi.stopLoadPlan(url, request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanInstance(), request.getLoadPlanInstanceRunCount(), request.getStopLevel());
    }   
    
    @POST
    @Path("/loadPlanStatus")
    @Produces({MediaType.APPLICATION_JSON})
    public List<LoadPlanStatusType> loadPlanStatus(LoadPlanStatusRequestDTO request) {
        url = getUrlODI();
        return webServiceOdi.loadPlanStatus(url, request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlans());
    }  


}
