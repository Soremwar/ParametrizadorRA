package co.com.claro.ejb.dao.parent;

import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Contiene toda la implementacion generica de todos los metodos estandar de DAO
 *
 * @author andres bedoya
 * @param <T>
 */
public abstract class AbstractJpaDAO<T> {

    private final Class<T> entityClass;

    public AbstractJpaDAO(Class entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
        
    }
    
    
    public void create2(T entity) throws Exception {
        try {
            getEntityManager().persist(entity);
        } catch (Exception e) {
            throw new Exception(e);
        }
        //getEntityManager().flush();

    }
    public T edit(T entity) {
        getEntityManager().merge(entity);
        //getEntityManager().flush();
        if (entity == null) {
            throw new DataNotFoundException("No se encontro la entidad " + entity);
        }
        return entity;
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    
    public T find(Object id) {
        getEntityManager().flush();
        Object item = getEntityManager().find(entityClass, id);
        if (item == null) {
            throw new DataNotFoundException("No se encontro el Registro " + id);
        }
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        List<T> lst = getEntityManager().createQuery(cq).getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }
        return lst;
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1]);// - range[0] + 1);
        q.setFirstResult(range[0]);
        List<T> lst = q.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
