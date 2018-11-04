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
public class LoadPlanRequestDTO implements Serializable {
    long loadPlanInstanceId;
    long loadPlanRunNumber;

    public long getLoadPlanInstanceId() {
        return loadPlanInstanceId;
    }

    public void setLoadPlanInstanceId(long loadPlanInstanceId) {
        this.loadPlanInstanceId = loadPlanInstanceId;
    }

    public long getLoadPlanRunNumber() {
        return loadPlanRunNumber;
    }

    public void setLoadPlanRunNumber(long loadPlanRunNumber) {
        this.loadPlanRunNumber = loadPlanRunNumber;
    }
    
 
    
}
