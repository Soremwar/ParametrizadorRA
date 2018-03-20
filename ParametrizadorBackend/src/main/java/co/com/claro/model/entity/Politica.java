/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andres
 */
@Entity
@Table(name = "TBL_POLITICA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Politica.findAll", query = "SELECT t FROM Politica t")
    , @NamedQuery(name = "Politica.findByCodPolitica", query = "SELECT t FROM Politica t WHERE t.codPolitica = :codPolitica")
    , @NamedQuery(name = "Politica.findByNombrePolitica", query = "SELECT t FROM Politica t WHERE t.nombrePolitica = :nombrePolitica")
    , @NamedQuery(name = "Politica.findByDescripcion", query = "SELECT t FROM Politica t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "Politica.findByObjetivo", query = "SELECT t FROM Politica t WHERE t.objetivo = :objetivo")
    , @NamedQuery(name = "Politica.findByFechaCreacion", query = "SELECT t FROM Politica t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Politica.findByFechaActualizacion", query = "SELECT t FROM Politica t WHERE t.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Politica.findByUsuario", query = "SELECT t FROM Politica t WHERE t.usuario = :usuario")})
public class Politica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_POLITICA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codPolitica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE_POLITICA")
    private String nombrePolitica;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "USUARIO")
    private String usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPolitica")
    private Collection<Conciliacion> tblConciliacionCollection;

    public Politica() {
    }

    public Politica(Integer codPolitica) {
        this.codPolitica = codPolitica;
    }

    public Politica(Integer codPolitica, String nombrePolitica, String descripcion, String objetivo, Date fechaCreacion, Date fechaActualizacion, String usuario) {
        this.codPolitica = codPolitica;
        this.nombrePolitica = nombrePolitica;
        this.descripcion = descripcion;
        this.objetivo = objetivo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.usuario = usuario;
    }

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
        hash += (codPolitica != null ? codPolitica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Politica)) {
            return false;
        }
        Politica other = (Politica) object;
        if ((this.codPolitica == null && other.codPolitica != null) || (this.codPolitica != null && !this.codPolitica.equals(other.codPolitica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.claro.parametrizador.Politica[ codPolitica=" + codPolitica + " ]";
    }
    
}
