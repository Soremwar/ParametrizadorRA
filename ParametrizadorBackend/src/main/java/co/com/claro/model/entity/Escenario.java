/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.EscenarioDTO;
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
 * @author andresbedoya
 */
@Entity
@Table(name = "TBL_ESCENARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Escenario.findAll", query = "SELECT t FROM Escenario t")
    , @NamedQuery(name = "Escenario.findByCodEscenario", query = "SELECT t FROM Escenario t WHERE t.id = :id")
    , @NamedQuery(name = "Escenario.findByNombreEscenario", query = "SELECT t FROM Escenario t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Escenario.findByImpacto", query = "SELECT t FROM Escenario t WHERE t.impacto = :impacto")
    , @NamedQuery(name = "Escenario.findByFechaCreacion", query = "SELECT t FROM Escenario t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Escenario.findByFechaActualizacion", query = "SELECT t FROM Escenario t WHERE t.fechaActualizacion = :fechaActualizacion")})
public class Escenario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_ESCENARIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_ESCENARIO")
    private String nombre;
    
    @Size(max = 100)
    @Column(name = "IMPACTO")
    private String impacto;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "USUARIO")
    private String usuario;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.DATE)
    private Date fechaActualizacion;
    
    @ManyToOne()
    @JoinColumn(name = "COD_CONCILIACION")
    private Conciliacion conciliacion;


    public Escenario() {
    }

    public Escenario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public Conciliacion getConciliacion() {
        return conciliacion;
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
        if (!(object instanceof Escenario)) {
            return false;
        }
        Escenario other = (Escenario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.model.entity.Escenario[ id=" + id + " ]";
    }
    
    
    public EscenarioDTO toDTO(){
        EscenarioDTO entidadDTO = new EscenarioDTO();
        entidadDTO.setId(this.getId());
        entidadDTO.setNombre(nombre);           
        entidadDTO.setConciliacion(this.getConciliacion() != null ? this.getConciliacion().toDTO() : null);
        entidadDTO.setFechaCreacion(fechaCreacion);
        entidadDTO.setFechaActualizacion(fechaActualizacion);
        entidadDTO.setImpacto(this.getImpacto());
        entidadDTO.setUsuario(this.getUsuario());
        
        return entidadDTO;
    }
}
