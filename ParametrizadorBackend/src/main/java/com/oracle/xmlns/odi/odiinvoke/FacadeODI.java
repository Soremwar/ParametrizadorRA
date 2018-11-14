/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.xmlns.odi.odiinvoke;

import co.com.claro.model.dto.request.LoadPlanRequestDTO;
import co.com.claro.model.dto.request.LoadPlanStartupParameterRequestDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceException;

/**
 *
 * @author andresbedoya
 */
public class FacadeODI {
    
    private URL getURL (String wsdlLocation) {
        //String wsdlLocation = "http://Server-ODI:8088/mockInvokeRequestSOAP11Binding?wsdl";
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL(wsdlLocation);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        return url;
    }


    public String getVersion(String wsdlLocation) {
        // TODO code application logic here
        OdiInvoke odi = new OdiInvoke(getURL(wsdlLocation));
        GetWebServiceVersionRequest requestVersion = new GetWebServiceVersionRequest();
        GetWebServiceVersionResponse responseVersion = odi.getOdiInvokeRequestSOAP11Port0().getVersion(requestVersion);
        return responseVersion.getVersion();
    }

    public OdiStartLoadPlanType startLoadPlan(String wsdlLocation, String odiUser, String odiPassword, String workRepository,
            String loadPlanName, String contexto, List<LoadPlanStartupParameterRequestDTO> params) {

        //Creando objeto Request
        OdiInvoke odi = new OdiInvoke(getURL(wsdlLocation));
        OdiStartLoadPlanRequest request = new OdiStartLoadPlanRequest();

        //Creando objeto Request Credentials
        OdiCredentialType odiCredentialType = new OdiCredentialType();
        odiCredentialType.setOdiUser(odiUser);
        odiCredentialType.setOdiPassword(odiPassword);
        odiCredentialType.setWorkRepository(workRepository);
        request.setCredentials(odiCredentialType);

        //Creando objeto Request StartLoadPlanRequestType
        StartLoadPlanRequestType startLoadPlanRequestType = new StartLoadPlanRequestType();
        startLoadPlanRequestType.setContext(contexto);
        startLoadPlanRequestType.setLoadPlanName(loadPlanName);
        request.setStartLoadPlanRequest(startLoadPlanRequestType);
        
        //List<VariableType> loadPlanStartupParameters = new ArrayList<>();
        for (LoadPlanStartupParameterRequestDTO param : params) {
            VariableType aux = new VariableType();
            aux.name = param.getNombre();
            aux.value = param.getValor();
            //loadPlanStartupParameters.add(aux);
            startLoadPlanRequestType.getLoadPlanStartupParameters().add(aux);
        }
        

        //Llamando Servicio
        OdiStartLoadPlanResponse response = odi.getOdiInvokeRequestSOAP11Port0().invokeStartLoadPlan(request);
        return response.getExecutionInfo();
    }

    public OdiStopLoadPlanType stopLoadPlan(String wsdlLocation, String odiUser, String odiPassword, String workRepository,
            long loadPlanInstance, long loadPlanInstanceRunCount, String stopLevel) {

        OdiInvoke odi = new OdiInvoke(getURL(wsdlLocation));
        OdiStopLoadPlanRequest request = new OdiStopLoadPlanRequest();

        //Creando objeto Request Credentials
        OdiCredentialType odiCredentialType = new OdiCredentialType();
        odiCredentialType.setOdiUser(odiUser);
        odiCredentialType.setOdiPassword(odiPassword);
        odiCredentialType.setWorkRepository(workRepository);
        request.setCredentials(odiCredentialType);

        //Creando objeto Request OdiStopLoadPlanRequest
        StopLoadPlanRequestType stopLoadPlanRequestType = new StopLoadPlanRequestType();
        stopLoadPlanRequestType.setLoadPlanInstanceId(loadPlanInstance);
        stopLoadPlanRequestType.setStopLevel(stopLevel);
        request.setOdiStopLoadPlanRequest(stopLoadPlanRequestType);

        //Llamando Servicio
        OdiStopLoadPlanResponse response = odi.getOdiInvokeRequestSOAP11Port0().invokeStopLoadPlan(request);
        return response.getExecutionInfo();
    }

    public List<LoadPlanStatusType> loadPlanStatus(String wsdlLocation, String odiUser, String odiPassword, String workRepository, List<LoadPlanRequestDTO> loadPlans) {
        OdiInvoke odi = new OdiInvoke(getURL(wsdlLocation));

        //Creando objeto Request Credentials
        OdiGetLoadPlanStatusRequest request = new OdiGetLoadPlanStatusRequest();
        OdiCredentialType odiCredentialType = new OdiCredentialType();
        odiCredentialType.setOdiUser(odiUser);
        odiCredentialType.setOdiPassword(odiPassword);
        odiCredentialType.setWorkRepository(workRepository);
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
