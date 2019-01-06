package co.com.claro.service.rest;

import co.com.claro.ejb.dao.ParametroDAO;
import co.com.claro.model.dto.ParametroDTO;
import co.com.claro.model.dto.request.LoadPlanStatusRequestDTO;
import co.com.claro.model.dto.request.StartLoadPlanRequestDTO;
import co.com.claro.model.dto.request.StopLoadPlanRequestDTO;
import co.com.claro.model.entity.Parametro;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import co.com.claro.service.rest.i18n.I18N;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;
import co.com.claro.service.rest.util.ResponseWrapper;

import com.oracle.xmlns.odi.odiinvoke.FacadeODI;
import com.oracle.xmlns.odi.odiinvoke.LoadPlanStatusType;
import com.oracle.xmlns.odi.odiinvoke.OdiStartLoadPlanType;
import com.oracle.xmlns.odi.odiinvoke.OdiStopLoadPlanType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.persistence.Transient;
import javax.ws.rs.POST;

/**
 * Clase que maneja el API Rest de Resultados
 * @author Andres Bedoya
 */
@Stateless
@Path("odiRest")
public class OdiREST{
    @Transient
    private static final Logger logger = Logger.getLogger(OdiREST.class.getSimpleName());

    @EJB
    protected ParametroDAO parametrosDAO;
    
    //@EJB(mappedName="stars21_web-Model-SessionEJB")
    protected FacadeODI facadeODI = new FacadeODI();
    
    //protected String wsdlLocation = getWsdlLocationODIFromDB(); // = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
    //"http://Server-ODI:8088/mockInvokeRequestSOAP11Binding?wsdl";
    
    @GET
    @Path("/getWsdlLocationODIFromDB")
    @JWTTokenNeeded
    public String getWsdlLocationODIFromDB() {
        String wsdlLocation;
        try {
            wsdlLocation = parametrosDAO.findByParametro("SISTEMA", "V_odiUrl");
        } catch (Exception e) {
            wsdlLocation = "http://172.24.42.164:8100/oraclediagent/OdiInvoke?wsdl";
        }
        return wsdlLocation;
    }  
    
    @GET
    @Path("/getOdiParametros")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public List<ParametroDTO> getOdiParametros() {
    	
        List<Parametro> lst = parametrosDAO.findByOdi("V_odi");
        List<ParametroDTO> lstDTO = new ArrayList<>();
        lst.forEach((entidad) -> {
            lstDTO.add(entidad.toDTO());
        });
        List<ParametroDTO> lstFinal = (List<ParametroDTO>) (List<?>) lstDTO;
        return lstFinal;
    }  
    
    @GET
    @Path("/getVersion")
    @JWTTokenNeeded
    public String getVersion() {
        return facadeODI.getVersion(getWsdlLocationODIFromDB());
        
    }  
    
    @POST
    @Path("/startLoadPlan")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response startLoadPlan(StartLoadPlanRequestDTO request) {
    	try {
    		Object[] cancelar = request.getParams().stream().filter( x -> x.getNombre().equals("PRY_GAI.V_0553_ESTADO") && x.getValor().equals("0")).toArray();
    		OdiStartLoadPlanType startLoadPlan = facadeODI.startLoadPlan(getWsdlLocationODIFromDB(), request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanName(), request.getContexto(), request.getParams());
    		
    		
    		if(cancelar == null || cancelar.length == 0) {
    			ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("resultados.rechazar"),startLoadPlan);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}else {
    			ResponseWrapper wraper = new ResponseWrapper(true,I18N.getMessage("resultados.aprovacion"),startLoadPlan);
        		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}
    		
    	}catch (Exception e) {
    		if(e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
    			logger.log(Level.SEVERE, e.getMessage(), e);
    			ResponseWrapper wraper = new ResponseWrapper(false,  e.getCause().getMessage(), 500);
    			return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}else {
    			logger.log(Level.SEVERE, e.getMessage(), e);
    			ResponseWrapper wraper = new ResponseWrapper(false,  I18N.getMessage("general.saveerror"), 500);
    			return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}
    	}
    }   

    @POST
    @Path("/stopLoadPlan")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public OdiStopLoadPlanType stopLoadPlan(StopLoadPlanRequestDTO request) {
        return facadeODI.stopLoadPlan(getWsdlLocationODIFromDB(), request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlanInstance(), request.getLoadPlanInstanceRunCount(), request.getStopLevel());
    }   
    
    @POST
    @Path("/loadPlanStatus")
    @JWTTokenNeeded
    @Produces({MediaType.APPLICATION_JSON})
    public Response loadPlanStatus(LoadPlanStatusRequestDTO request) {
    	try {
    		List<LoadPlanStatusType> responses = facadeODI.loadPlanStatus(getWsdlLocationODIFromDB(), request.getOdiUser(), request.getOdiPassword(), request.getWorkRepository(), request.getLoadPlans());
    		
    		ResponseWrapper wraper = new ResponseWrapper(true,responses);
    		return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    	}catch (Exception e) {
    		if(e.getCause() != null && (e.getCause() instanceof DataAlreadyExistException || e.getCause() instanceof DataNotFoundException)) {
    			logger.log(Level.SEVERE, e.getMessage(), e);
    			ResponseWrapper wraper = new ResponseWrapper(false,  e.getCause().getMessage(), 500);
    			return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}else {
    			logger.log(Level.SEVERE, e.getMessage(), e);
    			ResponseWrapper wraper = new ResponseWrapper(false,  I18N.getMessage("odiinvoke.execute"), 500);
    			return Response.ok(wraper,MediaType.APPLICATION_JSON).build();
    		}
    	}
        
    }  


}
