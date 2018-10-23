/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.WsTransformacion;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * WsTransformacionDTO
 * @author andresbedoya
 */
@XmlRootElement
public class WsTransformacionDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String nombreWs;
    private String paqueteWs;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Integer idConciliacion;

    public Integer getIdConciliacion() {
        return idConciliacion;
    }

    public void setIdConciliacion(Integer idConciliacion) {
        this.idConciliacion = idConciliacion;
    }
    private String nombreConciliacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreWs() {
        return nombreWs;
    }

    public void setNombreWs(String nombreWs) {
        this.nombreWs = nombreWs;
    }

    public String getPaqueteWs() {
        return paqueteWs;
    }

    public void setPaqueteWs(String paqueteWs) {
        this.paqueteWs = paqueteWs;
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


    public String getNombreConciliacion() {
        return nombreConciliacion;
    }

    public void setNombreConciliacion(String nombreConciliacion) {
        this.nombreConciliacion = nombreConciliacion;
    }
    
    public WsTransformacion toEntity(){
        WsTransformacion entity = new WsTransformacion();
        entity.setId(id);
        entity.setFechaActualizacion(fechaActualizacion);
        entity.setFechaCreacion(fechaCreacion);
        entity.setNombreWs(nombreWs);
        entity.setPaqueteWs(paqueteWs);
        entity.setConciliacion(this.idConciliacion != null ? new Conciliacion(this.idConciliacion) : null);
        return entity;
    }

    @Override
    public String toString() {
        return "WsTransformacionDTO{" + "id=" + id + ", nombreWs=" + nombreWs + ", paqueteWs=" + paqueteWs + ", fechaCreacion=" + fechaCreacion + '}';
    }

}
