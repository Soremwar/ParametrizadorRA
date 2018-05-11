/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Politica;
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
public class PoliticaDAO extends AbstractJpaDAO<Politica>{
    private static final Logger logger = Logger.getLogger(PoliticaDAO.class.getSimpleName());
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    @EJB
    protected ConciliacionDAO conciliacionDAO;
    
    public PoliticaDAO() {
        super(Politica.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Buscar la misma cadena en todos los campos descriptivos
     * @param busqueda Campo por el cual va a buscar en todos los campos descriptivos
     * @return Lista de Politicas que cumplan con el criterio
     */
    public List<Politica> findByAnyColumn(String busqueda){
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});    
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findByAnyColumn", Politica.class);
        query.setParameter("nombrePolitica", "%" + busqueda + "%");
        query.setParameter("descripcion", "%" + busqueda + "%");
        query.setParameter("objetivo", "%" + busqueda + "%");
        List<Politica> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    public List<Politica> findByAllTree(int[] range){
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findAllTree", Politica.class);
        //query.setParameter("nombrePolitica", "%" + busqueda + "%");
        List<Politica> results = query.getResultList();
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);           
        List<Politica> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }
    
    public Politica findByAllTreeById(Integer id){
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findAllTreeById", Politica.class);
        query.setParameter("idPolitica", id);
        Politica result = query.getSingleResult();
        if (result == null) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return result; 
    }
    
     /**
     * Buscar que coincidan todos los criterios de busqueda
     * @param nombre Campo nombre
     * @param descripcion Campo descripcion
     * @param objetivo Campo objetivo
     * @return Lista de Politicas que cumplan con el criterio
     */
    public List<Politica> findByColumn(String nombre, String descripcion, String objetivo){
        logger.log(Level.INFO, "nombre:{0}descripcion:{0}objetivo:{0}", new Object[]{nombre, descripcion, objetivo});
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findByColumn", Politica.class);
        query.setParameter("nombrePolitica", "%" + nombre + "%");
        query.setParameter("descripcion", "%" + descripcion + "%");
        query.setParameter("objetivo", "%" + objetivo + "%");
        List<Politica> results = query.getResultList();
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
     * @return Lista de Politicas que cumplan con el criterio
     */
    public List<Politica> findByAnyColumn(String busqueda, int offset, int limit){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}limit:{0}", new Object[]{busqueda, offset, limit});
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findByAnyColumn", Politica.class);
        query.setParameter("nombrePolitica", "%" + busqueda + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Politica> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    
    public List<Politica> findPoliticaSinConciliacion(){
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findPoliticaSinConciliacion", Politica.class);
        //query.setParameter("idConciliacion", );
        List<Politica> results = query.getResultList();    
        List<Politica> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }
       
}

