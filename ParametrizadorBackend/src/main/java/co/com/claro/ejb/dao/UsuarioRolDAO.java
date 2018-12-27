package co.com.claro.ejb.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Usuario;
import co.com.claro.model.entity.UsuarioRol;
import co.com.claro.service.rest.excepciones.DataNotFoundException;

@Stateless
public class UsuarioRolDAO extends AbstractJpaDAO<UsuarioRol>{
	
	private static final Logger logger = Logger.getLogger(UsuarioDAO.class.getSimpleName());

    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;


	public UsuarioRolDAO() {
		super(UsuarioRol.class);
		
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public UsuarioRol findUsuarioRol(Integer id) {
    	logger.log(Level.INFO, "busqueda:{0}", new Object[]{id});
        TypedQuery<UsuarioRol> query = em.createNamedQuery("UsuarioRol.findRol", UsuarioRol.class);
        query.setParameter("id", id);
        List<UsuarioRol> results = query.getResultList();   
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results.get(0);
    }
	
	public Integer deleteRol(Integer id) {
    	int count = 0;
    	try {
    		logger.log(Level.INFO, "eliminar id:{0}", new Object[]{id});
        	TypedQuery<UsuarioRol> query = em.createNamedQuery("UsuarioRol.deleteRol", UsuarioRol.class);
        	query.setParameter("id", id);
        	count = query.executeUpdate();
    	}catch(Exception ex) {
    		throw ex;
    	}  
    	return count;
    }

}
