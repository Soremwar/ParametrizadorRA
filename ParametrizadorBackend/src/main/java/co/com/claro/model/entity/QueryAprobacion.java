/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.QueryAprobacionDTO;
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
@Table(name = "TBL_GAI_APROBACION_QUERIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QueryAprobacion.findAll", query = "SELECT a FROM QueryAprobacion a")
    , @NamedQuery(name = "QueryAprobacion.findById", query = "SELECT a FROM QueryAprobacion a WHERE a.id = :id")
    , @NamedQuery(name = "QueryAprobacion.findByEstadoAprobacion", query = "SELECT a FROM QueryAprobacion a WHERE a.estadoAprobacion = :estadoAprobacion")
    , @NamedQuery(name = "QueryAprobacion.findByFechaCreacion", query = "SELECT a FROM QueryAprobacion a WHERE a.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "QueryAprobacion.findByFechaActualizacion", query = "SELECT a FROM QueryAprobacion a WHERE a.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "QueryAprobacion.findByUsuario", query = "SELECT a FROM QueryAprobacion a WHERE a.usuario = :usuario")
    , @NamedQuery(name = "QueryAprobacion.findByAnyColumn", query = "SELECT DISTINCT(q) FROM QueryAprobacion q WHERE lower(q.estadoAprobacion) LIKE lower(:estadoAprobacion) OR lower(q.conciliacion.nombre) LIKE lower(:nombreConciliacion)")})

public class QueryAprobacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "COD_APROBACION_QUERIES")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "ESTADO_APROBACION")
    private Integer estadoAprobacion;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    
    @Size(min = 1, max = 200)
    @Column(name = "USUARIO")
    private String usuario;
    

    @Column(name = "MENSAJE")
    private String mensaje;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_CONCILIACION")
    private Conciliacion conciliacion;
    
    public QueryAprobacion() {
    }

    public QueryAprobacion(Integer codAprobacionQueries) {
        this.id = codAprobacionQueries;
    }

    public QueryAprobacion(Integer codAprobacionQueries, Date fechaCreacion, String usuario) {
        this.id = codAprobacionQueries;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

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
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : Date.from(Instant.now());
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

    public Conciliacion getConciliacion() {
        return conciliacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setConciliacion(Conciliacion conciliacion) {
        this.conciliacion = conciliacion;
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
        if (!(object instanceof QueryAprobacion)) {
            return false;
        }
        QueryAprobacion other = (QueryAprobacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QueryAprobacion{" + "id=" + id + ", estadoAprobacion=" + estadoAprobacion + ", conciliacion=" + conciliacion + '}';
    }

    
    public QueryAprobacionDTO toDTO(){
        QueryAprobacionDTO entidadDTO = new QueryAprobacionDTO();
        entidadDTO.setId(id);
        entidadDTO.setFechaActualizacion(fechaActualizacion);
        entidadDTO.setFechaCreacion(fechaCreacion);
        entidadDTO.setNombreConciliacion(usuario);
        entidadDTO.setEstadoAprobacion(estadoAprobacion);
        entidadDTO.setUsuario(usuario);
        entidadDTO.setMensaje(mensaje);
        
        entidadDTO.setIdConciliacion(conciliacion != null ? conciliacion.getId() : null);
        entidadDTO.setNombreConciliacion(conciliacion != null ? conciliacion.getNombre() : null);
        
        
        return entidadDTO;
    }
}
