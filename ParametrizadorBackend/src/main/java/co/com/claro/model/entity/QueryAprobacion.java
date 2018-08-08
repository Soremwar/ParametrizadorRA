/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "TBL_GAI_APROBACION_QUERIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AprobacionQuery.findAll", query = "SELECT a FROM AprobacionQuery a")
    , @NamedQuery(name = "AprobacionQuery.findByCodAprobacionQueries", query = "SELECT a FROM AprobacionQuery a WHERE a.codAprobacionQueries = :codAprobacionQueries")
    , @NamedQuery(name = "AprobacionQuery.findByEstadoAprobacion", query = "SELECT a FROM AprobacionQuery a WHERE a.estadoAprobacion = :estadoAprobacion")
    , @NamedQuery(name = "AprobacionQuery.findByFechaCreacion", query = "SELECT a FROM AprobacionQuery a WHERE a.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "AprobacionQuery.findByFechaActualizacion", query = "SELECT a FROM AprobacionQuery a WHERE a.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "AprobacionQuery.findByUsuario", query = "SELECT a FROM AprobacionQuery a WHERE a.usuario = :usuario")})
public class QueryAprobacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_APROBACION_QUERIES")
    private Integer codAprobacionQueries;
    
    @Column(name = "ESTADO_APROBACION")
    private Integer estadoAprobacion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "USUARIO")
    private String usuario;

    public QueryAprobacion() {
    }

    public QueryAprobacion(Integer codAprobacionQueries) {
        this.codAprobacionQueries = codAprobacionQueries;
    }

    public QueryAprobacion(Integer codAprobacionQueries, Date fechaCreacion, String usuario) {
        this.codAprobacionQueries = codAprobacionQueries;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

    public Integer getCodAprobacionQueries() {
        return codAprobacionQueries;
    }

    public void setCodAprobacionQueries(Integer codAprobacionQueries) {
        this.codAprobacionQueries = codAprobacionQueries;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codAprobacionQueries != null ? codAprobacionQueries.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QueryAprobacion)) {
            return false;
        }
        QueryAprobacion other = (QueryAprobacion) object;
        if ((this.codAprobacionQueries == null && other.codAprobacionQueries != null) || (this.codAprobacionQueries != null && !this.codAprobacionQueries.equals(other.codAprobacionQueries))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.AprobacionQuery[ codAprobacionQueries=" + codAprobacionQueries + " ]";
    }
    
}
