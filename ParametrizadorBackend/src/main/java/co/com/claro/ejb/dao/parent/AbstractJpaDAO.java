package co.com.claro.ejb.dao.parent;

import co.com.claro.service.rest.excepciones.DataNotFoundException;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * Contiene toda la implementacion generica de todos los metodos estandar de DAO
 *
 * @author andres bedoya
 * @param <T>
 */
public abstract class AbstractJpaDAO<T extends Serializable> {

    private final Class<T> entityClass;

    public AbstractJpaDAO(Class entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {

        try {
            getEntityManager().persist(entity);
        } catch (ConstraintViolationException e) {
            // Aqui tira los errores de constraint
            for (ConstraintViolation actual : e.getConstraintViolations()) {
                System.out.println(actual.toString());
            }
        }
        //   getEntityManager().persist(entity);

    }

    public T edit(T entity) {
        try {
            getEntityManager().merge(entity);
            //getEntityManager().flush();
            if (entity == null) {
                throw new DataNotFoundException("No se encontro la entidad " + entity);
            }
            return entity;
        } catch (ConstraintViolationException e) {
            // Aqui tira los errores de constraint
            for (ConstraintViolation actual : e.getConstraintViolations()) {
                System.out.println(actual.toString());
            }
        }

        return null;
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
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();    
        Root<T> c = cq.from(entityClass);
        cq.select(c);
        cq.orderBy(cb.desc(c.get("id")));
        javax.persistence.Query query = getEntityManager().createQuery(cq);
        if(range != null) {
        	query.setMaxResults(range[1]);// - range[0] + 1);
            query.setFirstResult(range[0]);
        }
        
        List<T> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }
        return query.getResultList();
    }
    
    

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
