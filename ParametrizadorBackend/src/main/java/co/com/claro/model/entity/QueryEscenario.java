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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "TBL_GAI_QUERIES_ESCENARIOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QueryEscenario.findAll", query = "SELECT q FROM QueryEscenario q")
    , @NamedQuery(name = "QueryEscenario.findByCodQueryEscenario", query = "SELECT q FROM QueryEscenario q WHERE q.queryEscenarioPK.codQueryEscenario = :codQueryEscenario")
    , @NamedQuery(name = "QueryEscenario.findByNombreQuery", query = "SELECT q FROM QueryEscenario q WHERE q.nombreQuery = :nombreQuery")
    , @NamedQuery(name = "QueryEscenario.findByQuery", query = "SELECT q FROM QueryEscenario q WHERE q.query = :query")
    , @NamedQuery(name = "QueryEscenario.findByOrden", query = "SELECT q FROM QueryEscenario q WHERE q.orden = :orden")
    , @NamedQuery(name = "QueryEscenario.findByFechaCreacion", query = "SELECT q FROM QueryEscenario q WHERE q.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "QueryEscenario.findByFechaActualizacion", query = "SELECT q FROM QueryEscenario q WHERE q.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "QueryEscenario.findByUsuario", query = "SELECT q FROM QueryEscenario q WHERE q.usuario = :usuario")
    , @NamedQuery(name = "QueryEscenario.findByCodEscenario", query = "SELECT q FROM QueryEscenario q WHERE q.queryEscenarioPK.codEscenario = :codEscenario")})

public class QueryEscenario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected QueryEscenarioPK queryEscenarioPK;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE_QUERY")
    private String nombreQuery;
    
    @Size(max = 200)
    @Column(name = "QUERY")
    private String query;
    
    @Column(name = "ORDEN")
    private Integer orden;
    
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

    public QueryEscenario() {
    }

    public QueryEscenario(QueryEscenarioPK queryEscenarioPK) {
        this.queryEscenarioPK = queryEscenarioPK;
    }

    public QueryEscenario(QueryEscenarioPK queryEscenarioPK, String nombreQuery, Date fechaCreacion, String usuario) {
        this.queryEscenarioPK = queryEscenarioPK;
        this.nombreQuery = nombreQuery;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

    public QueryEscenario(int codQueryEscenario, int codEscenario) {
        this.queryEscenarioPK = new QueryEscenarioPK(codQueryEscenario, codEscenario);
    }

    public QueryEscenarioPK getQueryEscenarioPK() {
        return queryEscenarioPK;
    }

    public void setQueryEscenarioPK(QueryEscenarioPK queryEscenarioPK) {
        this.queryEscenarioPK = queryEscenarioPK;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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
        hash += (queryEscenarioPK != null ? queryEscenarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QueryEscenario)) {
            return false;
        }
        QueryEscenario other = (QueryEscenario) object;
        if ((this.queryEscenarioPK == null && other.queryEscenarioPK != null) || (this.queryEscenarioPK != null && !this.queryEscenarioPK.equals(other.queryEscenarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.QueryEscenario[ queryEscenarioPK=" + queryEscenarioPK + " ]";
    }
    
}
