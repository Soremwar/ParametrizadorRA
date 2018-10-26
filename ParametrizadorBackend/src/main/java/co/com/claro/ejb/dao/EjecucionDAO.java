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
import java.util.logging.Level;
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
    @Override
    public List<EjecucionProceso> findRange(int[] range){
        TypedQuery<EjecucionProceso> query = em.createNamedQuery("EjecucionProceso.findAll", EjecucionProceso.class);
        List<EjecucionProceso> results = query.getResultList();
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);           
        List<EjecucionProceso> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }
    
    /**
     * Retornar todos los registros
     * @param idEjecucion
     * @return Retorna todos los registros
     */
    public EjecucionProceso find(Integer idEjecucion){
        logger.log(Level.INFO, "idEjecucion:{0}", new Object[]{idEjecucion}); 
        TypedQuery<EjecucionProceso> query = em.createNamedQuery("EjecucionProceso.findByCodEjecucion", EjecucionProceso.class);
        em.flush();
        query.setParameter("codEjecucion", idEjecucion);
        EjecucionProceso item = query.getResultList() != null && query.getResultList().size() > 0 ? query.getResultList().get(0) : null;
        if (item == null) {
            throw new DataNotFoundException("No se encontro el Registro " + idEjecucion);
        }
        return item;
    }  
    

    
    /**
     * Retornar todos los registros
     * @return Retorna todos los registros
     */
    public List<EjecucionProceso> findByEscenario(Integer idEscenario){
        logger.log(Level.INFO, "codEscenario:{0}", new Object[]{idEscenario}); 
        TypedQuery<EjecucionProceso> query = em.createNamedQuery("EjecucionProceso.findByCodEscenario", EjecucionProceso.class);
        em.flush();
        query.setParameter("codEscenario", idEscenario);
        List<EjecucionProceso> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }  
    
        /**
     * Retornar todos los registros
     * @return Retorna todos los registros
     */
    public List<EjecucionProceso> findByPlanInstance(String idPlanInstance){
        logger.log(Level.INFO, "idPlanInstance:{0}", new Object[]{idPlanInstance}); 
        TypedQuery<EjecucionProceso> query = em.createNamedQuery("EjecucionProceso.findByIdPlanInstance", EjecucionProceso.class);
        em.flush();
        //query.setMaxResults(range[1]);// - range[0] + 1);
        //query.setFirstResult(range[0]);  
        query.setParameter("idPlanInstance", idPlanInstance);
        List<EjecucionProceso> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }   
}

