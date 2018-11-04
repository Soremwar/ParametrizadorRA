/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.soap.odi.producer;

import co.com.claro.ejb.dao.utils.Propiedades;
import co.com.claro.model.dto.request.LoadPlanRequestDTO;
import co.com.claro.service.soap.odi.consumer.GetWebServiceVersionRequest;
import co.com.claro.service.soap.odi.consumer.GetWebServiceVersionResponse;
import co.com.claro.service.soap.odi.consumer.LoadPlanStatusRequestType;
import co.com.claro.service.soap.odi.consumer.LoadPlanStatusType;
import co.com.claro.service.soap.odi.consumer.OdiCredentialType;
import co.com.claro.service.soap.odi.consumer.OdiGetLoadPlanStatusRequest;
import co.com.claro.service.soap.odi.consumer.OdiGetLoadPlanStatusResponse;
import co.com.claro.service.soap.odi.consumer.OdiInvoke;
import co.com.claro.service.soap.odi.consumer.OdiStartLoadPlanRequest;
import co.com.claro.service.soap.odi.consumer.OdiStartLoadPlanResponse;
import co.com.claro.service.soap.odi.consumer.OdiStartLoadPlanType;
import co.com.claro.service.soap.odi.consumer.OdiStopLoadPlanRequest;
import co.com.claro.service.soap.odi.consumer.OdiStopLoadPlanResponse;
import co.com.claro.service.soap.odi.consumer.OdiStopLoadPlanType;
import co.com.claro.service.soap.odi.consumer.StartLoadPlanRequestType;
import co.com.claro.service.soap.odi.consumer.StopLoadPlanRequestType;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @author andresbedoya
 */
@Stateless(name = "SessionEJBName", mappedName = "stars21_web-Model-SessionEJB")
@WebService(name = "WebServiceODIPortType", serviceName = "WebServiceODIdService")
public class WebServiceOdi {
    
    public static void main(String[] args) {
        
        WebServiceOdi c = new WebServiceOdi();
        OdiInvoke odi = new OdiInvoke();

        //getVersion()
        GetWebServiceVersionRequest requestVersion = new GetWebServiceVersionRequest();
        GetWebServiceVersionResponse responseVersion = odi.getOdiInvokeRequestSOAP11Port0().getVersion(requestVersion);
        System.out.println("getVersion()..." + c.getVersion());
        
        //startLoadPlans
        System.out.println("startLoadPlan()..." + c.startLoadPlan("odiUser", "odiPassword", "workRepository", "loadPlanName", "contexto"));
        
        //loadPlans
        List<LoadPlanRequestDTO> loadPlans = new ArrayList<>();
        System.out.println("loadStatus()..." + c.loadPlanStatus("odiUser", "odiPassword", "workRepository", loadPlans));

        //stopPlans
        System.out.println("loadStatus()..." + c.stopLoadPlan("odiUser", "odiPassword", "workRepository", 0, 0, "stopLevel"));
        
        Propiedades.loadProperties();
    }
    
    @WebMethod(operationName = "getVersion")
    public String getVersion() {
        // TODO code application logic here
        OdiInvoke odi = new OdiInvoke();
        GetWebServiceVersionRequest requestVersion = new GetWebServiceVersionRequest();
        GetWebServiceVersionResponse responseVersion = odi.getOdiInvokeRequestSOAP11Port0().getVersion(requestVersion);
        return responseVersion.getVersion();
    }
    
    @WebMethod(operationName = "startLoadPlan")
    @WebResult(name = "executionInfo")
    public OdiStartLoadPlanType startLoadPlan(@WebParam(name = "OdiUser") String odiUser, @WebParam(name = "odiPassword") String odiPassword, @WebParam(name = "workRepository") String workRepository, 
            @WebParam(name = "loadPlanName") String loadPlanName, @WebParam(name = "contexto") String contexto) {
        //Creando objeto Request
        OdiInvoke odi = new OdiInvoke();
        OdiStartLoadPlanRequest request = new OdiStartLoadPlanRequest();
        
        //Creando objeto Request Credentials
        OdiCredentialType odiCredentialType = new OdiCredentialType();
        odiCredentialType.setOdiUser(odiUser);
        odiCredentialType.setOdiPassword(odiPassword);
        request.setCredentials(odiCredentialType);
        
        //Creando objeto Request StartLoadPlanRequestType
        StartLoadPlanRequestType startLoadPlanRequestType = new StartLoadPlanRequestType();
        startLoadPlanRequestType.setContext(contexto);
        startLoadPlanRequestType.setLoadPlanName(loadPlanName);
        request.setStartLoadPlanRequest(startLoadPlanRequestType);
        
        //Llamando Servicio
        OdiStartLoadPlanResponse response = odi.getOdiInvokeRequestSOAP11Port0().invokeStartLoadPlan(request);
        return response.getExecutionInfo();
    }
    
    @WebMethod(operationName = "stopLoadPlan")
    @WebResult(name = "OdiStopLoadPlanResponse")
    public OdiStopLoadPlanType stopLoadPlan(@WebParam(name = "OdiUser") String odiUser, @WebParam(name = "odiPassword") String odiPassword, @WebParam(name = "workRepository") String workRepository, 
                @WebParam(name = "loadPlanInstance") long loadPlanInstance, @WebParam(name = "loadPlanInstanceRunCount") long loadPlanInstanceRunCount, @WebParam(name = "stopLevel") String stopLevel) {
        
        OdiInvoke odi = new OdiInvoke();        
        OdiStopLoadPlanRequest request = new OdiStopLoadPlanRequest();
        
        //Creando objeto Request Credentials
        OdiCredentialType odiCredentialType = new OdiCredentialType();
        odiCredentialType.setOdiUser(odiUser);
        odiCredentialType.setOdiPassword(odiPassword);
        request.setCredentials(odiCredentialType);
        
        //Creando objeto Request OdiStopLoadPlanRequest
        StopLoadPlanRequestType stopLoadPlanRequestType = new StopLoadPlanRequestType();
        stopLoadPlanRequestType.setLoadPlanInstanceId(loadPlanInstance);
        stopLoadPlanRequestType.setLoadPlanInstanceRunCount(loadPlanInstance);
        stopLoadPlanRequestType.setStopLevel(stopLevel);
        request.setOdiStopLoadPlanRequest(stopLoadPlanRequestType);
        
        //Llamando Servicio
        OdiStopLoadPlanResponse response = odi.getOdiInvokeRequestSOAP11Port0().invokeStopLoadPlan(request);
        return response.getExecutionInfo();
    }
    
    @WebMethod(operationName = "loadPlanStatus")
    //@WebResult(name = "OdiGetLoadPlanStatus")
    public List<LoadPlanStatusType> loadPlanStatus(@WebParam(name = "OdiUser") String odiUser, @WebParam(name = "odiPassword") String odiPassword, @WebParam(name = "workRepository") String workRepository,  List<LoadPlanRequestDTO> loadPlans) {
        OdiInvoke odi = new OdiInvoke();

        //Creando objeto Request Credentials
        OdiGetLoadPlanStatusRequest request = new OdiGetLoadPlanStatusRequest();
        OdiCredentialType odiCredentialType = new OdiCredentialType();
        odiCredentialType.setOdiUser(odiUser);
        odiCredentialType.setOdiPassword(odiPassword);
        request.setCredentials(odiCredentialType);
        
        //Creando objeto Request LoadPlans
        
        for (LoadPlanRequestDTO item : loadPlans) {
            LoadPlanStatusRequestType loadPlanStatusRequest = new LoadPlanStatusRequestType();
            loadPlanStatusRequest.setLoadPlanInstanceId(item.getLoadPlanInstanceId());
            loadPlanStatusRequest.setLoadPlanRunNumber(item.getLoadPlanRunNumber());
            request.getLoadPlans().add(loadPlanStatusRequest);
        }
        
        //Llamando Servicio
        OdiGetLoadPlanStatusResponse response = odi.getOdiInvokeRequestSOAP11Port0().getLoadPlanStatus(request);
        
        return response.getLoadPlanStatusResponse();
    }
    
    @WebMethod(operationName = "helloWorld")
    public String sayHelloWorld(String message) {
        try {
            System.out.println("sayHelloWorld:" + message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return "Here is the message: '" + message + "'";
    }
    
}
