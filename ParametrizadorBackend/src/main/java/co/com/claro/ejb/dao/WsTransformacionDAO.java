/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Resultado;
import co.com.claro.model.entity.WsTransformacion;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
public class WsTransformacionDAO extends AbstractJpaDAO<WsTransformacion>{
    private static final Logger logger = Logger.getLogger(WsTransformacionDAO.class.getSimpleName());
    
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    @EJB
    protected ConciliacionDAO conciliacionDAO;
    
    public WsTransformacionDAO() {
        super(WsTransformacion.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Buscar la misma cadena en todos los campos descriptivos
     * @param busqueda Campo por el cual va a buscar en todos los campos descriptivos
     * @return Lista de WsTransformacion que cumplan con el criterio
     */
    public List<WsTransformacion> findByAnyColumn(String busqueda){
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});    
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findByAnyColumn", WsTransformacion.class);
        query.setParameter("nombreWs", "%" + busqueda + "%");
        query.setParameter("paqueteWs", "%" + busqueda + "%");
        List<WsTransformacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    public List<WsTransformacion> findByAllTree(int[] range){
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findAllTree", WsTransformacion.class);
        //query.setParameter("nombreWsTransformacion", "%" + busqueda + "%");
        List<WsTransformacion> results = query.getResultList();
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);           
        List<WsTransformacion> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }
    
    public WsTransformacion findByAllTreeById(Integer id){
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findAllTreeById", WsTransformacion.class);
        query.setParameter("idWsTransformacion", id);
        em.flush();
        WsTransformacion result = query.getSingleResult();
        if (result == null) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return result; 
    }
            
    /**
     * Buscar el texto en todas columnas con paginado
     * @param busqueda cadena de texto por el cual va a buscar
     * @param offset desde que registro va a buscar
     * @param limit limite de registros
     * @return Lista de WsTransformaciones que cumplan con el criterio
     */
    public List<Resultado> findByAnyColumn(String busqueda, int offset, int limit){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}limit:{0}", new Object[]{busqueda, offset, limit});
        TypedQuery<Resultado> query = em.createNamedQuery("Resultado.findByAnyColumn", Resultado.class);
        query.setParameter("nombreResultado", "%" + busqueda + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Resultado> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
           
}

