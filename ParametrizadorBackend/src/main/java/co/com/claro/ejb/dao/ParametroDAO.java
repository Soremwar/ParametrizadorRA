/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.ejb.dao.parent.AbstractJpaDAO;
import co.com.claro.model.entity.Parametro;
import co.com.claro.service.rest.excepciones.DataNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
public class ParametroDAO extends AbstractJpaDAO<Parametro> {

    private static final Logger logger = Logger.getLogger(ParametroDAO.class.getSimpleName());
    @PersistenceContext(unitName = "co.com.claro_ParametrizadorClaro_war_1.0PU")
    private EntityManager em;

    public ParametroDAO() {
        super(Parametro.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Retornar todos los registros
     *
     * @param range
     * @return Retorna todos los registros
     */
    public List<Parametro> findByAllTree(int[] range) {
        TypedQuery<Parametro> query = em.createNamedQuery("Parametro.findAll", Parametro.class);
        query.setMaxResults(range[1]);// - range[0] + 1);
        query.setFirstResult(range[0]);
        List<Parametro> lst = query.getResultList();
        if (lst == null || lst.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos");
        }
        return query.getResultList();
    }

    public List<Parametro> findAVencer() {
        List<Parametro> parametroPeriodicidad = this.findByAnyColumn("periodicidadparamconciliacion");
        int periodicidad = 30; // 30 días es el valor por defecto
        if (parametroPeriodicidad.size() > 0) {
            try {
                periodicidad = Integer.parseInt(parametroPeriodicidad.get(0).getValor());
            } catch (Exception ex) {

            }
        }
        Date fechaActual = new Date();
          Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(fechaActual);   // assigns calendar to given date 
            calendar.add(Calendar.DAY_OF_MONTH, (periodicidad*-1));

        TypedQuery<Parametro> query = em.createNamedQuery("Parametro.findAVencer", Parametro.class);
        query.setParameter("fecha",fechaActual);
        List<Parametro> results = query.getResultList();
        System.out.println("A vencer:**"+results.size());
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;

    }

    /**
     * Buscar la misma cadena en todos los campos descriptivos
     *
     * @param busqueda Campo por el cual va a buscar en todos los campos
     * descriptivos
     * @return Lista de Parametros que cumplan con el criterio
     */
    public List<Parametro> findByAnyColumn(String busqueda) {
        logger.log(Level.INFO, "busqueda:{0}", new Object[]{busqueda});
        TypedQuery<Parametro> query = em.createNamedQuery("Parametro.findByAnyColumn", Parametro.class);
        query.setParameter("parametro", "%" + busqueda + "%");
        query.setParameter("descripcion", "%" + busqueda + "%");
        query.setParameter("tipo", "%" + busqueda + "%");
        query.setParameter("valor", "%" + busqueda + "%");
        List<Parametro> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }

    /**
     * Buscar un registro por un tupla de tipo y codpadre
     *
     * @param busqueda Campo por el cual va a buscar en todos los campos
     * descriptivos
     * @return Lista de Parametros que cumplan con el criterio
     */
    public List<Parametro> findByCodPadre(String tipo, Integer codPadre) {
        logger.log(Level.INFO, "tipo:{0}codPadre:{1}", new Object[]{tipo, codPadre});
        TypedQuery<Parametro> query = em.createNamedQuery("Parametro.findByCodPadre", Parametro.class);
        query.setParameter("tipo", "%" + tipo + "%");
        //query.setParameter("codigoPadre", "%" + codPadre + "%");
        List<Parametro> aux = query.getResultList();
        List<Parametro> results = codPadre != null ? aux.stream().filter(parametro -> codPadre.equals(parametro.getCodPadre())).collect(Collectors.toList()) : aux;
        if (results == null) {
            throw new DataNotFoundException("No se encontraron datos de Busqueda");
        }
        return results;
    }
    
    public List<Parametro> validarExistenciaParametro(String tipo, Integer codPadre) {
        logger.log(Level.INFO, "tipo:{0}codPadre:{1}", new Object[]{tipo, codPadre});
        TypedQuery<Parametro> query = em.createNamedQuery("Parametro.findByCodPadre", Parametro.class);
        query.setParameter("tipo", "%" + tipo + "%");
        List<Parametro> aux = query.getResultList();
        List<Parametro> results = codPadre != null ? aux.stream().filter(parametro -> codPadre.equals(parametro.getCodPadre())).collect(Collectors.toList()) : aux;
        if (results != null) {
            throw new DataNotFoundException("Ya existen parámetros asociados a este padre " + codPadre);
        }
        return results;
    }

    public String findByParametro(String tipo, String parametro) {
        logger.log(Level.INFO, "tipo:{0}nombre:{1}", new Object[]{tipo, parametro});
        TypedQuery<Parametro> query = em.createNamedQuery("Parametro.findByParametro", Parametro.class);
        query.setParameter("tipo", tipo);
        query.setParameter("parametro", parametro);
        List<Parametro> result = query.getResultList();
        return result.get(0).getValor();
    }

    public List<Parametro> findByOdi(String parametro){
        logger.log(Level.INFO, "nombre:{0}", new Object[]{parametro});     
        TypedQuery<Parametro> query = em.createNamedQuery("Parametro.findByOdi", Parametro.class);
        query.setParameter("parametro","%" + parametro + "%");
        List<Parametro> result = query.getResultList();
        logger.log(Level.INFO, result.get(0).toString());
        return result;
    }
   
}
