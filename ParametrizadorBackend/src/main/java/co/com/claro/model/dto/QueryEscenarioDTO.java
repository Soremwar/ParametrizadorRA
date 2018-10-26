/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.QueryEscenario;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para escenario
 * @author andres bedoya
 */
@XmlRootElement
public class QueryEscenarioDTO implements Serializable{

    //Campos padre
    private Integer id;
    private Integer idEscenario;
    private String nombreEscenario;
    private Integer idConciliacion;
    private String nombreConciliacion;
    
    //Campos entidad
    private String nombreQuery;
    private String query;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Integer orden;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombreQuery() {
        return nombreQuery;
    }

    public void setNombreQuery(String nombreQuery) {
        this.nombreQuery = nombreQuery;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getIdEscenario() {
        return idEscenario;
    }

    public void setIdEscenario(Integer idEscenario) {
        this.idEscenario = idEscenario;
    }

    public String getNombreEscenario() {
        return nombreEscenario;
    }

    public void setNombreEscenario(String nombreEscenario) {
        this.nombreEscenario = nombreEscenario;
    }

    public Integer getIdConciliacion() {
        return idConciliacion;
    }

    public void setIdConciliacion(Integer idConciliacion) {
        this.idConciliacion = idConciliacion;
    }

    public String getNombreConciliacion() {
        return nombreConciliacion;
    }

    public void setNombreConciliacion(String nombreConciliacion) {
        this.nombreConciliacion = nombreConciliacion;
    }

    public QueryEscenario toEntity(){
        QueryEscenario entity = new QueryEscenario();
        
        entity.setId(id);
        entity.setQuery(query);
        entity.setNombreQuery(nombreQuery);
        entity.setOrden(orden);
        entity.setFechaCreacion(fechaCreacion);
        entity.setFechaActualizacion(fechaActualizacion);

        return entity;
        
    }

    @Override
    public String toString() {
        return "QueryEscenarioDTO{" + "id=" + id + ", idEscenario=" + idEscenario + ", idConciliacion=" + idConciliacion + ", nombreQuery=" + nombreQuery + ", query=" + query + ", fechaCreacion=" + fechaCreacion + ", fechaActualizacion=" + fechaActualizacion + ", orden=" + orden + '}';
    }
    
    
}
