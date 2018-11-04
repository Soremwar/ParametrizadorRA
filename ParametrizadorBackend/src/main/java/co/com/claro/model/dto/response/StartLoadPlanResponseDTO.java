/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto.response;

import co.com.claro.model.dto.request.*;
import java.io.Serializable;

/**
 *
 * @author andresbedoya
 */
public class StartLoadPlanResponseDTO implements Serializable{
    String odiLoadPlanInstanceId;
    String runCount;
    String masterRepositoryId;
    String masterRepositoryTimestamp;

    public String getOdiLoadPlanInstanceId() {
        return odiLoadPlanInstanceId;
    }

    public void setOdiLoadPlanInstanceId(String odiLoadPlanInstanceId) {
        this.odiLoadPlanInstanceId = odiLoadPlanInstanceId;
    }

    public String getRunCount() {
        return runCount;
    }

    public void setRunCount(String runCount) {
        this.runCount = runCount;
    }

    public String getMasterRepositoryId() {
        return masterRepositoryId;
    }

    public void setMasterRepositoryId(String masterRepositoryId) {
        this.masterRepositoryId = masterRepositoryId;
    }

    public String getMasterRepositoryTimestamp() {
        return masterRepositoryTimestamp;
    }

    public void setMasterRepositoryTimestamp(String masterRepositoryTimestamp) {
        this.masterRepositoryTimestamp = masterRepositoryTimestamp;
    }

    @Override
    public String toString() {
        return "StartLoadPlanResponseDTO{" + "odiLoadPlanInstanceId=" + odiLoadPlanInstanceId + ", runCount=" + runCount + ", masterRepositoryId=" + masterRepositoryId + ", masterRepositoryTimestamp=" + masterRepositoryTimestamp + '}';
    }



    
    
}
