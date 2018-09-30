/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.LogAuditoria;
import co.com.claro.service.rest.Constantes;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para Resultado
 * @author andres
 */
@XmlRootElement
public class LogAuditoriaDTO implements Serializable{

    private Integer id;
    private String tabla;
    private String accion;
    private Date fechaCreacion;
    private String usuario;
    private String descripcion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    public LogAuditoria toEntity(){
        LogAuditoria entity = new LogAuditoria();
        
        entity.setId(id);
        entity.setTabla(tabla);
        entity.setAccion(accion);
        entity.setFechaCreacion(fechaCreacion);
        entity.setUsuario(usuario);
        entity.setDescripcion(descripcion);
       
        return entity;
    }
    
}
