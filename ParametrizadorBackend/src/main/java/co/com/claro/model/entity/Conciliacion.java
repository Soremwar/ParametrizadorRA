/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ConciliacionDTO;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    , @NamedQuery(name = "Conciliacion.findByCodConciliacion", query = "SELECT t FROM Conciliacion t WHERE t.codigo = :codConciliacion")
    , @NamedQuery(name = "Conciliacion.findByCamposTablaDestino", query = "SELECT t FROM Conciliacion t WHERE t.camposTablaDestino = :camposTablaDestino")
    , @NamedQuery(name = "Conciliacion.findByDescripcion", query = "SELECT t FROM Conciliacion t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "Conciliacion.findByFechaActualizacion", query = "SELECT t FROM Conciliacion t WHERE t.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Conciliacion.findByFechaCreacion", query = "SELECT t FROM Conciliacion t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Conciliacion.findByNombreConciliacion", query = "SELECT t FROM Conciliacion t WHERE t.nombre = :nombreConciliacion")
    , @NamedQuery(name = "Conciliacion.findByTablaDestino", query = "SELECT t FROM Conciliacion t WHERE t.tablaDestino = :tablaDestino")
    , @NamedQuery(name = "Conciliacion.findByUsuario", query = "SELECT t FROM Conciliacion t WHERE t.usuario = :usuario")
    , @NamedQuery(name = "Conciliacion.findByAnyColumn", query = "SELECT t FROM Conciliacion t WHERE lower(t.nombre) LIKE lower(:nombreConciliacion) or lower(t.descripcion) LIKE lower(:descripcion)")})
    
public class Conciliacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_CONCILIACION")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    
    @Column(name = "CAMPOS_TABLA_DESTINO")
    private String camposTablaDestino;
    
    @Size(max = 255)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Size(max = 255)
    @Column(name = "NOMBRE_CONCILIACION")
    private String nombre;
    
    @Size(max = 255)
    @Column(name = "TABLA_DESTINO")
    private String tablaDestino;
    
    @Size(max = 255)
    @Column(name = "USUARIO")
    private String usuario;
    
    @JoinColumn(name = "COD_POLITICA", referencedColumnName = "COD_POLITICA")
    @OneToOne
    private Politica politica;
    
    public Conciliacion() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCamposTablaDestino() {
        return camposTablaDestino;
    }

    public void setCamposTablaDestino(String camposTablaDestino) {
        this.camposTablaDestino = camposTablaDestino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTablaDestino() {
        return tablaDestino;
    }

    public void setTablaDestino(String tablaDestino) {
        this.tablaDestino = tablaDestino;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Politica getPolitica() {
        return politica;
    }

    public void setPolitica(Politica politica) {
        this.politica = politica;
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
        if (!(object instanceof Conciliacion)) {
            return false;
        }
        Conciliacion other = (Conciliacion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.Conciliacion[ codConciliacion=" + codigo + " ]";
    }
    
    public ConciliacionDTO toDTO(){
        ConciliacionDTO entidadDTO = new ConciliacionDTO();
        entidadDTO.setCodigo(this.getCodigo());
        entidadDTO.setNombre(this.getNombre());            
        entidadDTO.setPolitica(this.getPolitica() != null ? this.getPolitica().toDTO() : null);
        entidadDTO.setDescripcion(this.getDescripcion());
        entidadDTO.setFechaActualizacion(this.getFechaActualizacion());
        entidadDTO.setCamposTablaDestino(this.getCamposTablaDestino());

        entidadDTO.setTablaDestino(this.getTablaDestino());
        entidadDTO.setUsuario(this.getUsuario());
        
        return entidadDTO;
    }
  
}
