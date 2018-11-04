/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto.response;

import co.com.claro.model.dto.request.*;
import co.com.claro.service.soap.odi.consumer.LoadPlanStatusRequestType;
import java.util.List;

/**
 *
 * @author andresbedoya
 */
public class LoadPlantStatusResponseDTO {
    String odiUser;
    String odiPassword;
    String workRepository;



    public String getOdiUser() {
        return odiUser;
    }

    public void setOdiUser(String odiUser) {
        this.odiUser = odiUser;
    }

    public String getOdiPassword() {
        return odiPassword;
    }

    public void setOdiPassword(String odiPassword) {
        this.odiPassword = odiPassword;
    }

    public String getWorkRepository() {
        return workRepository;
    }

    public void setWorkRepository(String workRepository) {
        this.workRepository = workRepository;
    }
    
}
