/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.service.rest.Constantes;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresbedoya
 */
@Entity
@Table(name = "TBL_GAI_LOG_AUDITORIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogAuditoria.findAll", query = "SELECT l FROM LogAuditoria l")
    , @NamedQuery(name = "LogAuditoria.findByCodLogAuditoria", query = "SELECT l FROM LogAuditoria l WHERE l.id = :id")
    , @NamedQuery(name = "LogAuditoria.findByTabla", query = "SELECT l FROM LogAuditoria l WHERE l.tabla = :tabla")
    , @NamedQuery(name = "LogAuditoria.findByAccion", query = "SELECT l FROM LogAuditoria l WHERE l.accion = :accion")
    , @NamedQuery(name = "LogAuditoria.findByFechaCreacion", query = "SELECT l FROM LogAuditoria l WHERE l.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "LogAuditoria.findByUsuario", query = "SELECT l FROM LogAuditoria l WHERE l.usuario = :usuario")
    , @NamedQuery(name = "LogAuditoria.findByAnyColumn", query = "SELECT DISTINCT(l) FROM LogAuditoria l WHERE lower(l.tabla) LIKE lower(:tabla) or lower(l.accion) LIKE lower(:accion) or LOWER(l.usuario) LIKE lower(:usuario)")})
public class LogAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_LOG_AUDITORIA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TABLA")
    private String tabla;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ACCION")
    private String accion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Size(max = 30)
    @Column(name = "USUARIO")
    private String usuario;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public LogAuditoria() {
    }

    public LogAuditoria(Integer codLogAuditoria) {
        this.id = codLogAuditoria;
    }

    public LogAuditoria(String tabla, String accion, Date fechaCreacion, String usuario, String descripcion) {
        this.tabla = tabla;
        this.accion = accion;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
        this.descripcion= descripcion;
    }

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

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogAuditoria)) {
            return false;
        }
        LogAuditoria other = (LogAuditoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.model.entity.LogAuditoria[ codLogAuditoria=" + id + " ]";
    }

}
