/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
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
public class UsuarioDAO extends AbstractJpaDAO<Usuario> {

    private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getSimpleName());

    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    @EJB
    protected ConciliacionDAO conciliacionDAO;

    public UsuarioDAO() {
        super(Usuario.class);
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
    public List<Usuario> findByAnyColumn(String busqueda) {
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByAnyColumn", Usuario.class);
        query.setParameter("usuario", "%" + busqueda + "%");
        query.setParameter("nombre", "%" + busqueda + "%");
        query.setParameter("email", "%" + busqueda + "%");
        List<Usuario> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    
    public Usuario findByNombreUsuario(String nombreUsuario) {
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{nombreUsuario});
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByNombreUsuario", Usuario.class);
        query.setParameter("usuario", nombreUsuario);
        List<Usuario> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results.get(0);
    }
}
