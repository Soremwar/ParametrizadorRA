/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres
 */
@XmlRootElement
public class PoliticaDTO implements Serializable{
    private Integer codPolitica;
    private String nombrePolitica;
    private String descripcion;
    private String objetivo;
    private String usuario;
    private Date fechaCreacion;
    private Date fechaActualizacion;

  
    
    public Integer getCodPolitica() {
        return codPolitica;
    }

    public void setCodPolitica(Integer codPolitica) {
        this.codPolitica = codPolitica;
    }

    public String getNombrePolitica() {
        return nombrePolitica;
    }

    public void setNombrePolitica(String nombrePolitica) {
        this.nombrePolitica = nombrePolitica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
    
}
