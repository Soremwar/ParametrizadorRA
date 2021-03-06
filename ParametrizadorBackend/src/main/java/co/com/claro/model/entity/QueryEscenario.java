/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.QueryEscenarioDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    , @NamedQuery(name = "QueryEscenario.findByCodQueryEscenario", query = "SELECT q FROM QueryEscenario q WHERE q.id = :codQueryEscenario")
    , @NamedQuery(name = "QueryEscenario.findByNombreQuery", query = "SELECT q FROM QueryEscenario q WHERE q.nombreQuery = :nombreQuery")
    , @NamedQuery(name = "QueryEscenario.findByQuery", query = "SELECT q FROM QueryEscenario q WHERE q.query = :query")
    , @NamedQuery(name = "QueryEscenario.findByOrden", query = "SELECT q FROM QueryEscenario q WHERE q.orden = :orden")
    , @NamedQuery(name = "QueryEscenario.findByFechaCreacion", query = "SELECT q FROM QueryEscenario q WHERE q.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "QueryEscenario.findByFechaActualizacion", query = "SELECT q FROM QueryEscenario q WHERE q.fechaActualizacion = :fechaActualizacion")
    
    , @NamedQuery(name = "QueryEscenario.findByCodEscenario", query = "SELECT q FROM QueryEscenario q WHERE q.escenario.id = :codEscenario")
    
    , @NamedQuery(name = "QueryEscenario.findByCodConciliacion", query = "SELECT q FROM QueryEscenario q WHERE q.escenario.conciliacion.id = :codConciliacion")    
    , @NamedQuery(name = "QueryEscenario.findByAnyColumn", query = "SELECT DISTINCT(q) FROM QueryEscenario q WHERE lower(q.nombreQuery) LIKE lower(:nombreQuery) or lower(q.query) LIKE lower(:query) or lower(q.escenario.nombre) LIKE lower(:queryEscenarioNombre)")})

public class QueryEscenario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_QUERY_ESCENARIO")
    private Integer id;
    
    @Size(max = 200)
    @Column(name = "NOMBRE_QUERY")
    private String nombreQuery;
    
    @Column(name = "QUERY")
    private String query;
    
    @Column(name = "ORDEN")
    private Integer orden;

    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_ESCENARIO")
    private Escenario escenario;
    
    public QueryEscenario() {
    }

    public QueryEscenario(Integer id, String nombreQuery, Date fechaCreacion) {
        this.id = id;
        this.nombreQuery = nombreQuery;
        this.fechaCreacion = fechaCreacion;
    }

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
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : Date.from(Instant.now());
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Escenario getEscenario() {
        return escenario;
    }

    public void setEscenario(Escenario escenario) {
        this.escenario = escenario;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work  in the case the id fields are not set
        if (!(object instanceof QueryEscenario)) {
            return false;
        }
        QueryEscenario other = (QueryEscenario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QueryEscenario{" + "id=" + id + ", nombreQuery=" + nombreQuery + '}';
    }
    
    public QueryEscenarioDTO toDTO(){
        QueryEscenarioDTO entidadDTO = new QueryEscenarioDTO();
        
        entidadDTO.setId(this.getId());
        entidadDTO.setFechaCreacion(fechaCreacion);
        entidadDTO.setFechaActualizacion(fechaActualizacion);
        entidadDTO.setNombreQuery(nombreQuery);
        entidadDTO.setQuery(query);
        entidadDTO.setOrden(orden);
        entidadDTO.setIdEscenario(escenario != null ? escenario.getId() : null);
        entidadDTO.setNombreEscenario(escenario != null ? escenario.getNombre() : null);
        entidadDTO.setIdConciliacion(escenario != null && escenario.getConciliacion() != null ? escenario.getConciliacion().getId() : null);
        entidadDTO.setNombreConciliacion(escenario != null && escenario.getConciliacion() != null ? escenario.getConciliacion().getNombre() : null);

        return entidadDTO;
    }
}
