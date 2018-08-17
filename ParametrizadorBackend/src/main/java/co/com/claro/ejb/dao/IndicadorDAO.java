/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Indicador;
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
public class IndicadorDAO extends AbstractJpaDAO<Indicador>{
    private static final Logger logger = Logger.getLogger(IndicadorDAO.class.getSimpleName());
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    public IndicadorDAO() {
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
    public List<Indicador> findByAllTree(int[] range){
        TypedQuery<Indicador> query = em.createNamedQuery("Indicador.findAll", Indicador.class);
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);           
        List<Indicador> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }        
        return query.getResultList();
    }  

    /**
     * Buscar la misma cadena en todos los campos descriptivos
     * @param busqueda Campo por el cual va a buscar en todos los campos descriptivos
     * @return Lista de Indicadors que cumplan con el criterio
     */
    public List<Indicador> findByAnyColumn(String busqueda){
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});     
        TypedQuery<Indicador> query = em.createNamedQuery("Indicador.findByAnyColumn", Indicador.class);
        query.setParameter("nombre", "%" + busqueda + "%");
        query.setParameter("descripcion", "%" + busqueda + "%");
        query.setParameter("textoFormula", "%" + busqueda + "%");
        query.setParameter("nombreescenario", "%" + busqueda + "%");
        List<Indicador> results = query.getResultList();
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
    public Indicador findByAllTreeById(int id){
       logger.log(Level.INFO, "findByAllTreeById:{0}", new Object[]{id});  
        TypedQuery<Indicador> query = em.createNamedQuery("Indicador.findByCodIndicador", Indicador.class);
        query.setParameter("idIndicador", id);
        List<Indicador> results = query.getResultList();
        Indicador foundEntity = null;
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
     * @return Lista de Indicadors que cumplan con el criterio
     */
    public List<Indicador> findByAnyColumn(String busqueda, int offset, int limit){
        logger.log(Level.INFO, "busqueda:{0}offset:{0}offset:{0}", new Object[]{busqueda, offset, limit});  
        TypedQuery<Indicador> query = em.createNamedQuery("Indicador.findByAnyColumn", Indicador.class);
        query.setParameter("nombreIndicador", "%" + busqueda + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Indicador> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
        
}

