/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Politica;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * We define one subinterface for each entity type we want to persist, adding any entity specific methods we want. For example, if we'd like 
 * to be able to query all orders that have been added since a certain date, we can add such a method:
 * @author andres
 */

@Stateless
public class ConciliacionDAO extends AbstractJpaDAO<Conciliacion>{
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    public ConciliacionDAO() {
        super(Politica.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

