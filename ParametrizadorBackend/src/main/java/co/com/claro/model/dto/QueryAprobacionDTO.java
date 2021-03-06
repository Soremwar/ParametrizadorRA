/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.QueryAprobacion;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para escenario
 * @author andres
 */
@XmlRootElement
public class QueryAprobacionDTO implements Serializable{

    private Integer id;
    private Integer estadoAprobacion;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    //Campos padre
    private Integer idConciliacion;
    private String nombreConciliacion;
    private String mensaje;
    private String username;
    


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(Integer estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
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


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public QueryAprobacion toEntity(){
        QueryAprobacion entity = new QueryAprobacion();
        //Campos comunes
        entity.setId(id);
        entity.setEstadoAprobacion(estadoAprobacion);
        entity.setFechaCreacion(fechaCreacion);
        entity.setFechaActualizacion(fechaActualizacion);
        entity.setMensaje(mensaje);
        entity.setUsuario(username);
        
        return entity;
        
    }

    @Override
    public String toString() {
        return "QueryAprobacionDTO{" + "id=" + id + ", estadoAprobacion=" + estadoAprobacion + ", fechaCreacion=" + fechaCreacion + ", fechaActualizacion=" + fechaActualizacion + '}';
    }
    
    
}
