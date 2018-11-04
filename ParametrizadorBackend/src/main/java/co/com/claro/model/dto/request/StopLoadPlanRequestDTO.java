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
public class StopLoadPlanRequestDTO implements Serializable{
    String odiUser;
    String odiPassword;
    String workRepository;
    long loadPlanInstance; 
    long loadPlanInstanceRunCount;
    String stopLevel;

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

    public long getLoadPlanInstance() {
        return loadPlanInstance;
    }

    public void setLoadPlanInstance(long loadPlanInstance) {
        this.loadPlanInstance = loadPlanInstance;
    }

    public long getLoadPlanInstanceRunCount() {
        return loadPlanInstanceRunCount;
    }

    public void setLoadPlanInstanceRunCount(long loadPlanInstanceRunCount) {
        this.loadPlanInstanceRunCount = loadPlanInstanceRunCount;
    }

    public String getStopLevel() {
        return stopLevel;
    }

    public void setStopLevel(String stopLevel) {
        this.stopLevel = stopLevel;
    }
    
}
