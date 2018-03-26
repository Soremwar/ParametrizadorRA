/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.service.rest.excepciones.DatosNoEncontrados;
import static java.lang.Math.log;
import java.util.List;
import java.util.Set;
import javax.faces.validator.Validator;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * It will have basic implementation of all the methods in the standard Dao
 * @author andres
 * @param <T>
 */
public abstract class AbstractJpaDAO<T>{


    private final Class<T> entityClass;

    public AbstractJpaDAO(Class entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        try {
            getEntityManager().persist(entity);
        } catch (ConstraintViolationException e) {
            System.out.println("co.com.claro.ejb.dao.AbstractJpaDAO.create()" + e.getStackTrace());
        }
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        Object item = getEntityManager().find(entityClass, id);
        if (item == null) {
            throw new DatosNoEncontrados("No se encontro el Registro " + id);
        }
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        List<T> lst = getEntityManager().createQuery(cq).getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DatosNoEncontrados("No se encontraron datos");
        }
        return lst;
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1]);// - range[0] + 1);
        q.setFirstResult(range[0]);
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
