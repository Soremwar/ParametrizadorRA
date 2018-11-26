/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Rol;
import co.com.claro.model.entity.Usuario;
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
 * Definimos una subinterface por cada entidad que queremos persistir. para asi
 * poder agregar metodos propios de esta entidad y heredar los generales.
 *
 * @author Andres Bedoya
 */
@Stateless
public class RolDAO extends AbstractJpaDAO<Rol> {

    private static final Logger logger = Logger.getLogger(RolDAO.class.getSimpleName());

    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    @EJB
    protected ConciliacionDAO conciliacionDAO;

    public RolDAO() {
        super(Rol.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    public Rol findByNombre(String nombre) {
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{nombre});
        TypedQuery<Rol> query = em.createNamedQuery("Rol.findByNombre", Rol.class);
        query.setParameter("nombre", nombre);
        List<Rol> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results.get(0);
    }

}
