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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres
 */
@Entity
@Table(name = "TBL_CONCILIACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conciliacion.findAll", query = "SELECT t FROM Conciliacion t")
    , @NamedQuery(name = "Conciliacion.findByCodConciliacion", query = "SELECT t FROM Conciliacion t WHERE t.codConciliacion = :codConciliacion")
    , @NamedQuery(name = "Conciliacion.findByNombreConciliacion", query = "SELECT t FROM Conciliacion t WHERE t.nombreConciliacion = :nombreConciliacion")
    , @NamedQuery(name = "Conciliacion.findByDescripcion", query = "SELECT t FROM Conciliacion t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "Conciliacion.findByTablaDestino", query = "SELECT t FROM Conciliacion t WHERE t.tablaDestino = :tablaDestino")
    , @NamedQuery(name = "Conciliacion.findByCamposTablaDestino", query = "SELECT t FROM Conciliacion t WHERE t.camposTablaDestino = :camposTablaDestino")
    , @NamedQuery(name = "Conciliacion.findByFechaCreacion", query = "SELECT t FROM Conciliacion t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Conciliacion.findByFechaActualizacion", query = "SELECT t FROM Conciliacion t WHERE t.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Conciliacion.findByUsuario", query = "SELECT t FROM Conciliacion t WHERE t.usuario = :usuario")})
public class Conciliacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_CONCILIACION")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codConciliacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE_CONCILIACION")
    private String nombreConciliacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "TABLA_DESTINO")
    private String tablaDestino;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CAMPOS_TABLA_DESTINO")
    private String camposTablaDestino;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.DATE)
    private Date fechaActualizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "USUARIO")
    private String usuario;
    @JoinColumn(name = "COD_POLITICA", referencedColumnName = "COD_POLITICA")
    @ManyToOne(optional = false)
    private Politica codPolitica;

    public Conciliacion() {
    }

    public Conciliacion(Integer codConciliacion) {
        this.codConciliacion = codConciliacion;
    }

    public Conciliacion(Integer codConciliacion, String nombreConciliacion, String descripcion, String tablaDestino, String camposTablaDestino, Date fechaCreacion, Date fechaActualizacion, String usuario) {
        this.codConciliacion = codConciliacion;
        this.nombreConciliacion = nombreConciliacion;
        this.descripcion = descripcion;
        this.tablaDestino = tablaDestino;
        this.camposTablaDestino = camposTablaDestino;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.usuario = usuario;
    }

    public Integer getCodConciliacion() {
        return codConciliacion;
    }

    public void setCodConciliacion(Integer codConciliacion) {
        this.codConciliacion = codConciliacion;
    }

    public String getNombreConciliacion() {
        return nombreConciliacion;
    }

    public void setNombreConciliacion(String nombreConciliacion) {
        this.nombreConciliacion = nombreConciliacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTablaDestino() {
        return tablaDestino;
    }

    public void setTablaDestino(String tablaDestino) {
        this.tablaDestino = tablaDestino;
    }

    public String getCamposTablaDestino() {
        return camposTablaDestino;
    }

    public void setCamposTablaDestino(String camposTablaDestino) {
        this.camposTablaDestino = camposTablaDestino;
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

    public Politica getCodPolitica() {
        return codPolitica;
    }

    public void setCodPolitica(Politica codPolitica) {
        this.codPolitica = codPolitica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codConciliacion != null ? codConciliacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conciliacion)) {
            return false;
        }
        Conciliacion other = (Conciliacion) object;
        if ((this.codConciliacion == null && other.codConciliacion != null) || (this.codConciliacion != null && !this.codConciliacion.equals(other.codConciliacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.claro.parametrizador.Conciliacion[ codConciliacion=" + codConciliacion + " ]";
    }
    
}
