/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.PoliticaDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author andres
 */
@Entity
@Table(name = "TBL_POLITICA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Politica.findNumRegistros", query = "SELECT COUNT(t) FROM Politica t")
    , @NamedQuery(name = "Politica.findAll", query = "SELECT t FROM Politica t")
    , @NamedQuery(name = "Politica.findByCodPolitica", query = "SELECT t FROM Politica t WHERE t.codigo = :codPolitica")
    , @NamedQuery(name = "Politica.findByNombrePolitica", query = "SELECT t FROM Politica t WHERE t.nombre LIKE :nombrePolitica")
    , @NamedQuery(name = "Politica.findByDescripcion", query = "SELECT t FROM Politica t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "Politica.findByObjetivo", query = "SELECT t FROM Politica t WHERE t.objetivo = :objetivo")
    , @NamedQuery(name = "Politica.findByFechaCreacion", query = "SELECT t FROM Politica t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Politica.findByFechaActualizacion", query = "SELECT t FROM Politica t WHERE t.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Politica.findByColumn", query = "SELECT t FROM Politica t WHERE lower(t.nombre) LIKE lower(:nombrePolitica) and lower(t.descripcion) LIKE lower(:descripcion) and lower(t.objetivo) LIKE lower(:objetivo)")
    , @NamedQuery(name = "Politica.findByUsuario", query = "SELECT t FROM Politica t WHERE t.usuario = :usuario")
    , @NamedQuery(name = "Politica.findByAnyColumn", query = "SELECT t FROM Politica t WHERE lower(t.nombre) LIKE lower(:nombrePolitica) or lower(t.descripcion) LIKE lower(:descripcion) or lower(t.objetivo) LIKE lower(:objetivo)")})

public class Politica implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "COD_POLITICA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE_POLITICA")
    private String nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "DESCRIPCION")
    
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "OBJETIVO")
    private String objetivo;
    
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
    
    @OneToOne(cascade = CascadeType.ALL,  mappedBy = "politica")
    private Collection<Conciliacion> tblConciliacionCollection;

    public Politica() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        if (fechaCreacion != null) {
            this.fechaCreacion = fechaCreacion;
        }
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

    @XmlTransient
    public Collection<Conciliacion> getTblConciliacionCollection() {
        return tblConciliacionCollection;
    }

    public void setTblConciliacionCollection(Collection<Conciliacion> tblConciliacionCollection) {
        this.tblConciliacionCollection = tblConciliacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Politica)) {
            return false;
        }
        Politica other = (Politica) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    public PoliticaDTO toDTO() {
        PoliticaDTO entidadDTO = new PoliticaDTO();
        entidadDTO.setCodigo(this.getCodigo());
        entidadDTO.setDescripcion(this.getDescripcion());
        entidadDTO.setFechaCreacion(this.getFechaCreacion());
        entidadDTO.setFechaActualizacion(this.getFechaActualizacion());
        entidadDTO.setNombre(this.getNombre());
        entidadDTO.setObjetivo(this.getObjetivo());
        entidadDTO.setUsuario(this.getUsuario());
        entidadDTO.setConciliacion(this.getConciliacion());
        return entidadDTO;
    }
    
    @Override
    public String toString() {
        return "com.claro.parametrizador.Politica[ codPolitica=" + codigo + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Conciliacion> getConciliacionCollection() {
        return tblConciliacionCollection;
    }

    public void setConciliacionCollection(Collection<Conciliacion> tblConciliacionCollection) {
        this.tblConciliacionCollection = tblConciliacionCollection;
    }
    
}
