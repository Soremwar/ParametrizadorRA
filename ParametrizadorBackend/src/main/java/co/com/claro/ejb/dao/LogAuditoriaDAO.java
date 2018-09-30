/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

/**
 * Definimos una subinterface por cada entidad que queremos persistir. para asi poder
 * agregar metodos propios de esta entidad y heredar los generales.
 * @author Andres Bedoya
 */

@Stateless
public class LogAuditoriaDAO extends AbstractJpaDAO<LogAuditoria>{
    private static final Logger logger = Logger.getLogger(LogAuditoriaDAO.class.getSimpleName());
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    public LogAuditoriaDAO() {
        super(Conciliacion.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Buscar la misma cadena en todos los campos descriptivos
     * @param busqueda Campo por el cual va a buscar en todos los campos descriptivos
     * @return Lista de Conciliacions que cumplan con el criterio
     */
    public List<LogAuditoria> findByAnyColumn(String busqueda){
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});     
        TypedQuery<LogAuditoria> query = em.createNamedQuery("LogAuditoria.findByAnyColumn", LogAuditoria.class);
        query.setParameter("tabla", "%" + busqueda + "%");
        query.setParameter("usuario", "%" + busqueda + "%");
        query.setParameter("accion", "%" + busqueda + "%");
        List<LogAuditoria> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
   
    /**
     * Buscar el texto en todas columnas con paginado
     * @param busqueda cadena de texto por el cual va a buscar
     * @param offset desde que registro va a buscar
     * @param limit limite de registros
     * @return Lista de Conciliacions que cumplan con el criterio
     */
    public List<LogAuditoria> findByAnyColumn(String busqueda, int offset, int limit){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}offset:{0}", new Object[]{busqueda, offset, limit});  
       TypedQuery<LogAuditoria> query = em.createNamedQuery("LogAuditoria.findByAnyColumn", LogAuditoria.class);
        query.setParameter("tabla", "%" + busqueda + "%");
        query.setParameter("usuario", "%" + busqueda + "%");
        query.setParameter("accion", "%" + busqueda + "%");        
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<LogAuditoria> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }    
}

