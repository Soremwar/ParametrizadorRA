/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.model.entity.EjecucionProceso;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.TypedQuery;

/**
 *
 * @author ronal
 */
public interface IEjecucionDAO {

    public EjecucionProceso find(Integer idEjecucion);

    public List<EjecucionProceso> findByEscenario(Integer idEscenario);

    public List<EjecucionProceso> findByPlanInstance(String idPlanInstance);

    public List<EjecucionProceso> findRange(int[] range);

    public void create(EjecucionProceso entity);

    public EjecucionProceso edit(EjecucionProceso entity);
    
    public void remove(EjecucionProceso entity);
    
    public int count();
}
