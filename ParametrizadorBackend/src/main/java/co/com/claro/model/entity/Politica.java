/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.PoliticaDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "TBL_GAI_POLITICA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Politica.findNumRegistros", query = "SELECT COUNT(p) FROM Politica p")
    , @NamedQuery(name = "Politica.findAll", query = "SELECT DISTINCT(p) FROM Politica p JOIN p.conciliaciones c")
    , @NamedQuery(name = "Politica.findAllTree", query = "SELECT DISTINCT(p) FROM Politica p LEFT JOIN FETCH p.conciliaciones c")
    , @NamedQuery(name = "Politica.findAllTreeById", query = "SELECT DISTINCT(p) FROM Politica p LEFT JOIN FETCH p.conciliaciones c WHERE p.id = :idPolitica")
    , @NamedQuery(name = "Politica.findPoliticaSinConciliacion", query = "SELECT DISTINCT(p) FROM Politica p LEFT JOIN p.conciliaciones c WHERE c.id IS NULL")
    , @NamedQuery(name = "Politica.findPoliticaSinConciliacionByName", query = "SELECT DISTINCT(p) FROM Politica p LEFT JOIN p.conciliaciones c WHERE c.id IS NULL and p.nombre like :name")
    , @NamedQuery(name = "Politica.findByCodPolitica", query = "SELECT DISTINCT(p) FROM Politica p WHERE p.id = :codPolitica")
    , @NamedQuery(name = "Politica.findByNombrePolitica", query = "SELECT DISTINCT(p) FROM Politica p WHERE p.nombre LIKE :nombrePolitica")
    , @NamedQuery(name = "Politica.findByDescripcion", query = "SELECT DISTINCT(p) FROM Politica p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "Politica.findByObjetivo", query = "SELECT DISTINCT(p) FROM Politica p WHERE p.objetivo = :objetivo")
    , @NamedQuery(name = "Politica.findByFechaCreacion", query = "SELECT DISTINCT(p) FROM Politica p WHERE p.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Politica.findByFechaActualizacion", query = "SELECT DISTINCT(p) FROM Politica p WHERE p.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Politica.findByColumn", query = "SELECT DISTINCT(p) FROM Politica p WHERE lower(p.nombre) LIKE lower(:nombrePolitica) and lower(p.descripcion) LIKE lower(:descripcion) and lower(p.objetivo) LIKE lower(:objetivo)")
    , @NamedQuery(name = "Politica.findByAnyColumn", query = "SELECT DISTINCT(p) FROM Politica p LEFT JOIN FETCH p.conciliaciones c WHERE lower(p.id) LIKE lower(:id) or lower(p.nombre) LIKE lower(:nombrePolitica) or lower(p.descripcion) LIKE lower(:descripcion) or lower(p.objetivo) LIKE lower(:objetivo) or lower(c.nombre) LIKE lower(:nombreConciliacion) or lower(c.id) LIKE lower(:idConciliacion) or lower(c.usuarioAsignado) LIKE lower(:usuarioAsignado)")})

public class Politica implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "COD_POLITICA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE_POLITICA")
    private String nombre;
    
    @Basic(optional = false)
    @Size(min = 1, max = 400)
    @Column(name = "DESCRIPCION")    
    private String descripcion;
    
    @Basic(optional = false)
    @Size(min = 1, max = 200)
    @Column(name = "OBJETIVO")
    private String objetivo;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlTransient
    private Date fechaCreacion;

    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlTransient
    private Date fechaActualizacion;
       
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "politica")
    //@OneToOne(mappedBy = "politica", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    private Collection<Conciliacion> conciliaciones;
    
    //@OneToOne(mappedBy = "politica", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    //private Conciliacion conciliaciones;

    public Politica() {
    }

    
    public Politica(Integer id) {
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
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : Date.from(Instant.now());
    }
    
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public void addConciliaciones(Conciliacion conciliacion) {
        this.conciliaciones.add(conciliacion);
        conciliacion.setPolitica(this);
    }
    
    public void removeConciliaciones(Conciliacion conciliacion) {
        this.conciliaciones.remove(conciliacion);
        conciliacion.setPolitica(this);
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
        if (!(object instanceof Politica)) {
            return false;
        }
        Politica other = (Politica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public PoliticaDTO toDTO() {
        PoliticaDTO entidadDTO = new PoliticaDTO();
        //Campos comunes
        entidadDTO.setId(this.getId());
        entidadDTO.setFechaCreacion(this.getFechaCreacion());
        entidadDTO.setFechaActualizacion(this.getFechaActualizacion());
        entidadDTO.setNombre(this.getNombre());
        
        //Campos de la entidad
        entidadDTO.setObjetivo(objetivo);
        entidadDTO.setDescripcion(descripcion);
        if (conciliaciones != null) {            
            Set<ConciliacionDTO> lstConciliaciones = conciliaciones.stream().map((itemDTO) -> itemDTO.toDTO()).sorted(Comparator.comparing(ConciliacionDTO::getId).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
            entidadDTO.setConciliaciones(lstConciliaciones);
        }

        return entidadDTO;
    }

    @Override
    public String toString() {
        return "Politica{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    


}
