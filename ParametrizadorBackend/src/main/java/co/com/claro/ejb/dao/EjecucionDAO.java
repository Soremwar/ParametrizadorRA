/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Indicador;
import co.com.claro.model.entity.EjecucionProceso;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Definimos una subinterface por cada entidad que queremos persistir. para asi poder
 * agregar metodos propios de esta entidad y heredar los generales.
 * @author Andres Bedoya
 */

@Stateless
public class EjecucionDAO extends AbstractJpaDAO<EjecucionProceso>{
    private static final Logger logger = Logger.getLogger(EjecucionDAO.class.getSimpleName());
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    public EjecucionDAO() {
        super(Indicador.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Retornar todos los registros
     * @param range
     * @return Retorna todos los registros
     */
    public List<EjecucionProceso> findByEscenario(Integer codEscenario){
        TypedQuery<EjecucionProceso> query = em.createNamedQuery("EjecucionProceso.findByCodEscenario", EjecucionProceso.class);
        em.flush();
        //query.setMaxResults(range[1]);// - range[0] + 1);
        //query.setFirstResult(range[0]);  
        query.setParameter("codEscenario", codEscenario);
        List<EjecucionProceso> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }          
}

