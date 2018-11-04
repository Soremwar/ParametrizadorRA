/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto.request;

import java.io.Serializable;

/**
 *
 * @author andresbedoya
 */
public class StartLoadPlanRequestDTO implements Serializable{
    String odiUser;
    String odiPassword;
    String workRepository;
    String loadPlanName;
    String contexto;

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

    public String getLoadPlanName() {
        return loadPlanName;
    }

    public void setLoadPlanName(String loadPlanName) {
        this.loadPlanName = loadPlanName;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }
    
    
    
}
