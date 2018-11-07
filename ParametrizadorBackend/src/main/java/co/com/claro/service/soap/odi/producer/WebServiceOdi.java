/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.soap.odi.producer;

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
import java.util.List;

/**
 *
 * @author andresbedoya
 */
public class WebServiceOdi {

    
    public String getVersion(String wsdl) {
        // TODO code application logic here
        OdiInvoke odi = new OdiInvoke(wsdl);
        GetWebServiceVersionRequest requestVersion = new GetWebServiceVersionRequest();
        GetWebServiceVersionResponse responseVersion = odi.getOdiInvokeRequestSOAP11Port0().getVersion(requestVersion);
        return responseVersion.getVersion();
    }
    

    public OdiStartLoadPlanType startLoadPlan(String wsdl, String odiUser, String odiPassword, String workRepository, 
            String loadPlanName, String contexto) {

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
    
    public OdiStopLoadPlanType stopLoadPlan(String wsdl, String odiUser, String odiPassword, String workRepository, 
                long loadPlanInstance, long loadPlanInstanceRunCount,  String stopLevel) {
        
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
    

    public List<LoadPlanStatusType> loadPlanStatus(String wsdl, String odiUser, String odiPassword, String workRepository,  List<LoadPlanRequestDTO> loadPlans) {
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

    
}
