/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Escenario;
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
public class EscenarioDAO extends AbstractJpaDAO<Escenario>{
    private static final Logger logger = Logger.getLogger(EscenarioDAO.class.getSimpleName());
    
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;
    

    public EscenarioDAO() {
        super(Escenario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Buscar la misma cadena en todos los campos descriptivos
     * @param busqueda Campo por el cual va a buscar en todos los campos descriptivos
     * @return Lista de Escenarios que cumplan con el criterio
     */
    public List<Escenario> findByAnyColumn(String busqueda){
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});    
        TypedQuery<Escenario> query = em.createNamedQuery("Escenario.findByAnyColumn", Escenario.class);
        query.setParameter("id", "%" + busqueda + "%");
        query.setParameter("nombreEscenario", "%" + busqueda + "%");
        query.setParameter("impacto", "%" + busqueda + "%");
        query.setParameter("descripcion", "%" + busqueda + "%");
        query.setParameter("nombreConciliacion", "%" + busqueda + "%");
        query.setParameter("nombreEscenario", "%" + busqueda + "%");     
        
        List<Escenario> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    /**
     * Retornar todo el arbol de la jerarquia
     * @param range rango de registros a retornar
     * @return Retornar todo el arbol de la jerarquia de esta entidad
     */
    public List<Escenario> findByAllTree(int[] range){
        TypedQuery<Escenario> query = em.createNamedQuery("Escenario.findAllTree", Escenario.class);
        //query.setParameter("nombreEscenario", "%" + busqueda + "%");
        List<Escenario> results = query.getResultList();
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);           
        List<Escenario> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }  
    
        /**
     * Busca la jerarquia por un id
     * @param id identificador unico a buscar
     * @return Retorna un item con su jerarquia
     */
    
     /**
     * Buscar que coincidan todos los criterios de busqueda
     * @param nombre Campo nombre
     * @param descripcion Campo descripcion
     * @param objetivo Campo objetivo
     * @return Lista de Escenarios que cumplan con el criterio
     */
    public List<Escenario> findByColumn(String nombre, String descripcion, String objetivo){
        logger.log(Level.INFO, "nombre:{0}descripcion:{0}objetivo:{0}", new Object[]{nombre, descripcion, objetivo});        
        TypedQuery<Escenario> query = em.createNamedQuery("Escenario.findByColumn", Escenario.class);
        query.setParameter("nombreEscenario", "%" + nombre + "%");
        query.setParameter("impacto", "%" + descripcion + "%");
        List<Escenario> results = query.getResultList();
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
     * @return Lista de Escenarios que cumplan con el criterio
     */
    public List<Escenario> findByAnyColumn(String busqueda, int offset, int limit){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}limit:{0}", new Object[]{busqueda, offset, limit}); 
        TypedQuery<Escenario> query = em.createNamedQuery("Escenario.findByAnyColumn", Escenario.class);
        query.setParameter("nombreEscenario", "%" + busqueda + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Escenario> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }

    /**
     * Encuentra los escenarios que no tienen asociada ninguna Conciliacion
     * @return Listado con las conciliaciones
     */
    public List<Escenario> findByPoliticaNull(){
        TypedQuery<Escenario> query = em.createNamedQuery("Escenario.findByConciliacionNull", Escenario.class);
        List<Escenario> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }    
    
    public List<Escenario> findByConciliacion(int idConciliacion){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}limit:{0}", new Object[]{idConciliacion});
        TypedQuery<Escenario> query = em.createNamedQuery("Escenario.findByConciliacion", Escenario.class);
        query.setParameter("codConciliacion", idConciliacion);
        List<Escenario> results = query.getResultList();
        return results;
    }
    
    
    public List<Escenario> findByName(String name){
    	TypedQuery<Escenario> query = em.createNamedQuery("Escenario.findByName", Escenario.class);
    	query.setParameter("name", "%" + name + "%");
    	return query.getResultList();
    }

}

