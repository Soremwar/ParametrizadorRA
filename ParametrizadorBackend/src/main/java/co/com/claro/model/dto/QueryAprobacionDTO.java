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
    private String usuario;
    private String mensaje;
    


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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public QueryAprobacion toEntity(){
        QueryAprobacion entity = new QueryAprobacion();
        //Campos comunes
        entity.setId(id);
        entity.setEstadoAprobacion(estadoAprobacion);
        entity.setFechaCreacion(fechaCreacion);
        entity.setFechaActualizacion(fechaActualizacion);
        entity.setUsuario(usuario);
        entity.setMensaje(mensaje);
        
        return entity;
        
    }
}
