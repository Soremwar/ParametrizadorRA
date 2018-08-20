/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.QueryEscenario;
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
public class QueryEscenarioDAO extends AbstractJpaDAO<QueryEscenario>{
    private static final Logger logger = Logger.getLogger(QueryEscenarioDAO.class.getSimpleName());
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    public QueryEscenarioDAO() {
        super(QueryEscenario.class);
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
    public List<QueryEscenario> findByAll(int[] range){
        TypedQuery<QueryEscenario> query = em.createNamedQuery("QueryEscenario.findAll", QueryEscenario.class);
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);           
        List<QueryEscenario> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }  

    /**
     * Buscar la misma cadena en todos los campos descriptivos
     * @param busqueda Campo por el cual va a buscar en todos los campos descriptivos
     * @return Lista de QueryEscenarios que cumplan con el criterio
     */
    public List<QueryEscenario> findByAnyColumn(String busqueda){
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});     
        TypedQuery<QueryEscenario> query = em.createNamedQuery("QueryEscenario.findByAnyColumn", QueryEscenario.class);
        query.setParameter("nombreQuery", "%" + busqueda + "%");
        query.setParameter("query", "%" + busqueda + "%");
        query.setParameter("queryEscenarioNombre", "%" + busqueda + "%");
        List<QueryEscenario> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
        
    
     /**
     * Buscar el texto en todas columnas con paginado
     * @param codQueryEscenario identificador     
     * @return Lista de QueryEscenarios que cumplan con el criterio
     */
    /*public QueryEscenario findById(Integer codQueryEscenario){
        logger.log(Level.INFO, "codQueryEscenario:{0}", new Object[]{codQueryEscenario});  
        TypedQuery<QueryEscenario> query = em.createNamedQuery("QueryEscenario.findByCodQueryEscenario", QueryEscenario.class);
        query.setParameter("codQueryEscenario", "%" + codQueryEscenario + "%");
        List<QueryEscenario> results = query.getResultList();
        QueryEscenario foundEntity = null;
        if(!results.isEmpty()){
            // ignores multiple results
            foundEntity = results.get(0);
        }
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return foundEntity;
    }*/
        
}

