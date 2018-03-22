/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.model.entity.Politica;
import co.com.claro.service.rest.excepciones.DatosNoEncontrados;
import java.util.List;
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
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

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
    public List<Politica> findByAnyColumn(String busqueda) {
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findByAnyColumn", Politica.class);
        query.setParameter("nombrePolitica", "%" + busqueda + "%");
        query.setParameter("descripcion", "%" + busqueda + "%");
        query.setParameter("objetivo", "%" + busqueda + "%");
        List<Politica> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DatosNoEncontrados("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    
     /**
     * Buscar que coincidan todos los criterios de busqueda
     * @param nombre Campo nombre
     * @param descripcion Campo descripcion
     * @param objetivo Campo objetivo
     * @return Lista de Politicas que cumplan con el criterio
     */
    public List<Politica> findByColumn(String nombre, String descripcion, String objetivo) {
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findByColumn", Politica.class);
        query.setParameter("nombrePolitica", "%" + nombre + "%");
        query.setParameter("descripcion", "%" + descripcion + "%");
        query.setParameter("objetivo", "%" + objetivo + "%");
        List<Politica> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DatosNoEncontrados("No se encontraron datos de Busqueda");
        }
        return results;
    }
        
    public List<Politica> findByAnyColumn(String busqueda, int offset, int limit) {
        TypedQuery<Politica> query = em.createNamedQuery("Politica.findByAnyColumn", Politica.class);
        query.setParameter("nombrePolitica", "%" + busqueda + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<Politica> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DatosNoEncontrados("No se encontraron datos de Busqueda");
        }
        return results;
    }
}

