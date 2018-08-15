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
    
    //Campos entidad
    private String nombreQuery;
    private String query;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Integer orden;
    private String usuario;

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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }


    public QueryEscenario toEntity(){
        QueryEscenario entity = new QueryEscenario();
        
        entity.setId(id);
        entity.setQuery(query);
        entity.setNombreQuery(nombreQuery);
        entity.setFechaCreacion(fechaCreacion);
        entity.setFechaActualizacion(fechaActualizacion);
        entity.setUsuario(usuario);
    
        return entity;
        
    }
}
