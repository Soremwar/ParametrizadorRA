/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.webservice.odi;

/**
 *
 * @author andresbedoya
 */
public class NewClass {
    public static void main(String[] args) {
        // TODO code application logic here
        OdiInvoke odi = new OdiInvoke();
        GetWebServiceVersionRequest requestVersion = new GetWebServiceVersionRequest();
        GetWebServiceVersionResponse responseVersion = odi.getOdiInvokeRequestSOAP11Port0().getVersion(requestVersion);
        System.out.println("responseVersion.getVersion()..." + responseVersion.getVersion());
        
        OdiGetLoadPlanStatusRequest requestPlanStatus = new OdiGetLoadPlanStatusRequest();
        OdiGetLoadPlanStatusResponse responsePlanStatus = odi.getOdiInvokeRequestSOAP11Port0().getLoadPlanStatus(requestPlanStatus);
        System.out.println("responsePlanStatus.getLoadPlanStatusResponse() ... " + responsePlanStatus.getLoadPlanStatusResponse().get(0).loadPlanMessage);
        
        //System.out.println("co.com.claro.webservice.odi.NewClass.main()" + response.getLoadPlanStatusResponse().);
        //System.out.println("co.com.claro.webservice.odi.NewClass.main()" + odi.getOdiInvokeRequestSOAP11Port0().getLoadPlanStatus(request).getLoadPlanStatusResponse().toString());
        
    }
    
}
