/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.ResConciliacion;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Definimos una subinterface por cada entidad que queremos persistir. para asi
 * poder agregar metodos propios de esta entidad y heredar los generales.
 *
 * @author Andres Bedoya
 */
@Stateless
public class ResConciliacionDAO extends AbstractJpaDAO<ResConciliacion> {

    private static final Logger logger = Logger.getLogger(ResConciliacionDAO.class.getSimpleName());

    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    public ResConciliacionDAO() {
        super(ResConciliacion.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Buscar la misma cadena en todos los campos descriptivos
     *
     * @param busqueda Campo por el cual va a buscar en todos los campos
     * descriptivos
     * @return Lista de Politicas que cumplan con el criterio
     */
    public List<ResConciliacion> findByAnyColumn(String busqueda) {
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});
        TypedQuery<ResConciliacion> query = em.createNamedQuery("ResConciliacion.findByAnyColumn", ResConciliacion.class);
        query.setParameter("id", "%" + busqueda + "%");
        query.setParameter("estado", "%" + busqueda + "%");
        query.setParameter("codConciliacion", "%" + busqueda + "%");
        query.setParameter("codEscenario", "%" + busqueda + "%");
        List<ResConciliacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }

    public List<ResConciliacion> findResConciliacionPorAprobar() {
        TypedQuery<ResConciliacion> query = em.createNamedQuery("ResConciliacion.findByEstado", ResConciliacion.class);
        //query.setParameter("idConciliacion", );
        List<ResConciliacion> results = query.getResultList();
        List<ResConciliacion> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }
        return query.getResultList();
    }

}
