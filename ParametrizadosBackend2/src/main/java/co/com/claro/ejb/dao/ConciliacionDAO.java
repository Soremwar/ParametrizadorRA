/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Conciliacion;
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
public class ConciliacionDAO extends AbstractJpaDAO<Conciliacion>{
    private static final Logger logger = Logger.getLogger(ConciliacionDAO.class.getSimpleName());
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU", type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    public ConciliacionDAO() {
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
    public List<Conciliacion> findByAnyColumn(String busqueda){
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});     
        TypedQuery<Conciliacion> query = em.createNamedQuery("Conciliacion.findByAnyColumn", Conciliacion.class);
        query.setParameter("nombreConciliacion", "%" + busqueda + "%");
        query.setParameter("descripcion", "%" + busqueda + "%");
        query.setParameter("nombrePolitica", "%" + busqueda + "%");
        List<Conciliacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
   
    
    /**
     * Busca la jerarquia por un id
     * @param id identificador unico a buscar
     * @return Retorna un item con su jerarquia
     */
    public Conciliacion findByAllTreeById(int id){
        TypedQuery<Conciliacion> query = em.createNamedQuery("Conciliacion.findAllTreeById", Conciliacion.class);
        query.setParameter("idConciliacion", id);
        //Conciliacion result = query.getSingleResult();
        //em.getEntityManagerFactory().getCache().evictAll();
        List<Conciliacion> results = query.getResultList();
        Conciliacion foundEntity = null;
        if(!results.isEmpty()){
            // ignores multiple results
            foundEntity = results.get(0);
        }
        if (foundEntity == null) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return foundEntity; 
    }
        
        /**
     * Buscar el texto en todas columnas con paginado
     * @param busqueda cadena de texto por el cual va a buscar
     * @param offset desde que registro va a buscar
     * @param limit limite de registros
     * @return Lista de Conciliacions que cumplan con el criterio
     */
    /*public List<Conciliacion> findRange(int offset, int limit){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}offset:{0}", new Object[]{offset, limit});  
        TypedQuery<Conciliacion> query = em.createNamedQuery("Conciliacion.findByAnyColumn", Conciliacion.class);
        //query.setParameter("nombreConciliacion", "%" + busqueda + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Conciliacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }*/
    
    /**
     * Buscar el texto en todas columnas con paginado
     * @param busqueda cadena de texto por el cual va a buscar
     * @param offset desde que registro va a buscar
     * @param limit limite de registros
     * @return Lista de Conciliacions que cumplan con el criterio
     */
    public List<Conciliacion> findByAnyColumn(String busqueda, int offset, int limit){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}offset:{0}", new Object[]{busqueda, offset, limit});  
        TypedQuery<Conciliacion> query = em.createNamedQuery("Conciliacion.findByAnyColumn", Conciliacion.class);
        query.setParameter("nombreConciliacion", "%" + busqueda + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Conciliacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    
    public List<Conciliacion> findByAllTree(int[] range){
        TypedQuery<Conciliacion> query = em.createNamedQuery("Conciliacion.findAllTree", Conciliacion.class);
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);           
        List<Conciliacion> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }  
    
    /**
     * Encuentra las conciliaciaciones que no tienen asociada ninguna politica
     * @return Listado con las conciliaciones
     */
    public List<Conciliacion> findByPoliticaNull(){
        TypedQuery<Conciliacion> query = em.createNamedQuery("Conciliacion.findByPoliticaNull", Conciliacion.class);
        List<Conciliacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    public List<Conciliacion> findByPolitica(int politica){
        //logger.log(Level.INFO, "busqueda:{0}offset:{0}limit:{0}", new Object[]{busqueda, offset, limit});
        TypedQuery<Conciliacion> query = em.createNamedQuery("Conciliacion.findByPolitica", Conciliacion.class);
        query.setParameter("codPolitica", politica);
        List<Conciliacion> results = query.getResultList();
        /*if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }*/
        return results;
    }
}

