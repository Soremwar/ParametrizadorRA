/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.WsTransformacion;
import co.com.claro.service.rest.excepciones.DataAlreadyExistException;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.Date;
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
public class WsTransformacionDAO extends AbstractJpaDAO<WsTransformacion> implements IWsTransformacionDAO {

    private static final Logger logger = Logger.getLogger(WsTransformacionDAO.class.getSimpleName());

    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    @EJB
    protected ConciliacionDAO conciliacionDAO;

    public WsTransformacionDAO() {
        super(WsTransformacion.class);
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
     * @return Lista de WsTransformacion que cumplan con el criterio
     */
    public List<WsTransformacion> findByAnyColumn(String busqueda) {
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findByAnyColumn", WsTransformacion.class);
        query.setParameter("nombreWs", "%" + busqueda + "%");
        query.setParameter("paqueteWs", "%" + busqueda + "%");
        List<WsTransformacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }

    public List<WsTransformacion> findByAllTree(int[] range) {
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findAllTree", WsTransformacion.class);
        //query.setParameter("nombreWsTransformacion", "%" + busqueda + "%");
        List<WsTransformacion> results = query.getResultList();
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);
        List<WsTransformacion> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }
        return query.getResultList();
    }

    public WsTransformacion findByAllTreeById(Integer id) {
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findAllTreeById", WsTransformacion.class);
        query.setParameter("idWsTransformacion", id);
        em.flush();
        WsTransformacion result = query.getSingleResult();
        if (result == null) {
            throw new DataNotFoundException("No se encontraron datos");
        }
        return result;
    }

    public List<WsTransformacion> verificarSiExistePaqueteWs(String paqueteWs) {
        logger.log(Level.INFO, "paqueteWs:{0}", new Object[]{paqueteWs});
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findByPaqueteWs", WsTransformacion.class);
        query.setParameter("paqueteWs", paqueteWs);
        List<WsTransformacion> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            throw new DataAlreadyExistException("El paquete " + paqueteWs + " ya esta siendo utilizado");
        }
        return results;
    }

    public List<WsTransformacion> validPaqueteWs(String paqueteWs) {
        logger.log(Level.INFO, "paqueteWs:{0}", new Object[]{paqueteWs});
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findByPaqueteWs", WsTransformacion.class);
        query.setParameter("paqueteWs", paqueteWs);
        List<WsTransformacion> results = query.getResultList();
        if (results != null && !results.isEmpty() && !results.get(0).getPaqueteWs().equalsIgnoreCase(paqueteWs)) {
            throw new DataAlreadyExistException("El paquete " + paqueteWs + " ya esta siendo utilizado");
        }
        return results;
    }

    /**
     * Buscar por fecha de agendamiento
     *
     * @param fechaAgendamiento Campo por el cual va a buscar la fecha de
     * agendamiento
     * @return Lista de WsTransformacion que coincidan con la fecha (sin horas
     * ni minutos) de agendamiento
     */
    public List<WsTransformacion> findByFechaAgendamiento(Date fechaAgendamientoDesde, Date fechaAgendamientoHasta) {
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findByFechaAgendamiento", WsTransformacion.class);
        query.setParameter("fechaAgendamientoDesde", fechaAgendamientoDesde);
        query.setParameter("fechaAgendamientoHasta", fechaAgendamientoHasta);
        List<WsTransformacion> results = query.getResultList();
        /*    if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }*/
        return results;
    }
    
    public List<WsTransformacion> findAgendadas() {
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findAgendadas", WsTransformacion.class);
        List<WsTransformacion> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    public List<WsTransformacion> findByCodWs(int id) {
        TypedQuery<WsTransformacion> query = em.createNamedQuery("WsTransformacion.findByCodWs", WsTransformacion.class);
        query.setParameter("codWs", id);
        List<WsTransformacion> results = query.getResultList();
       /* if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }*/
        return results;
    }

}
