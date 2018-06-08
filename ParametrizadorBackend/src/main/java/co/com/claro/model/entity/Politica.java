/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.entity.parent.Padre;
import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.PoliticaDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
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
    @NamedQuery(name = "Politica.findNumRegistros", query = "SELECT COUNT(t) FROM Politica t")
    , @NamedQuery(name = "Politica.findAll", query = "SELECT DISTINCT(t) FROM Politica t JOIN t.conciliaciones c")
    , @NamedQuery(name = "Politica.findAllTree", query = "SELECT DISTINCT(t) FROM Politica t LEFT JOIN FETCH t.conciliaciones c")
    , @NamedQuery(name = "Politica.findAllTreeById", query = "SELECT DISTINCT(t) FROM Politica t LEFT JOIN FETCH t.conciliaciones c WHERE t.id = :idPolitica")
    , @NamedQuery(name = "Politica.findPoliticaSinConciliacion", query = "SELECT DISTINCT(t) FROM Politica t LEFT JOIN t.conciliaciones c WHERE c.id IS NULL")
    , @NamedQuery(name = "Politica.findByCodPolitica", query = "SELECT DISTINCT(t) FROM Politica t WHERE t.id = :codPolitica")
    , @NamedQuery(name = "Politica.findByNombrePolitica", query = "SELECT DISTINCT(t) FROM Politica t WHERE t.nombre LIKE :nombrePolitica")
    , @NamedQuery(name = "Politica.findByDescripcion", query = "SELECT DISTINCT(t) FROM Politica t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "Politica.findByObjetivo", query = "SELECT DISTINCT(t) FROM Politica t WHERE t.objetivo = :objetivo")
    , @NamedQuery(name = "Politica.findByFechaCreacion", query = "SELECT DISTINCT(t) FROM Politica t WHERE t.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Politica.findByFechaActualizacion", query = "SELECT DISTINCT(t) FROM Politica t WHERE t.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Politica.findByColumn", query = "SELECT DISTINCT(t) FROM Politica t WHERE lower(t.nombre) LIKE lower(:nombrePolitica) and lower(t.descripcion) LIKE lower(:descripcion) and lower(t.objetivo) LIKE lower(:objetivo)")
    , @NamedQuery(name = "Politica.findByUsuario", query = "SELECT DISTINCT(t) FROM Politica t WHERE t.usuario = :usuario")
    , @NamedQuery(name = "Politica.findByAnyColumn", query = "SELECT DISTINCT(t) FROM Politica t WHERE lower(t.nombre) LIKE lower(:nombrePolitica) or lower(t.descripcion) LIKE lower(:descripcion) or lower(t.objetivo) LIKE lower(:objetivo)")})

public class Politica extends Padre implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
    @Id
    @Basic(optional = false)
    @Column(name = "COD_POLITICA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    */
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
    @XmlTransient
    private Date fechaCreacion;

    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlTransient
    private Date fechaActualizacion;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "USUARIO")
    private String usuario;
    
    @JsonIgnore
    @OneToMany(fetch=FetchType.EAGER, mappedBy = "politica", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Collection<Conciliacion> conciliaciones;


    public Politica() {
    }

    
    public Politica(Integer id) {
        this.id = id;
    }
    /*
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
*/
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
    public Collection<Conciliacion> getConciliaciones() {
        return conciliaciones;
    }

    public void setConciliaciones(Collection<Conciliacion> conciliaciones) {
        this.conciliaciones = conciliaciones;
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
        entidadDTO.setUsuario(usuario);
        entidadDTO.setDescripcion(descripcion);
        if (conciliaciones != null) {
            List<ConciliacionDTO> lstConciliaciones = conciliaciones.stream().map((conciliacionDTO) -> conciliacionDTO.toDTO()).collect(toList());
            entidadDTO.setConciliaciones(lstConciliaciones);
        }

        return entidadDTO;
    }
    
    @Override
    public String toString() {
        return "com.claro.parametrizador.Politica[ codPolitica=" + id + " ]";
    }

}
