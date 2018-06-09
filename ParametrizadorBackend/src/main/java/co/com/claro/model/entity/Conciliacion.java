/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.EscenarioDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andres
 */
@Entity
@Table(name = "TBL_GAI_CONCILIACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conciliacion.findAll", query = "SELECT DISTINCT(c) FROM Conciliacion c")
    , @NamedQuery(name = "Conciliacion.findAllTree", query = "SELECT DISTINCT(c) FROM Conciliacion c LEFT JOIN FETCH c.escenarios e ORDER BY c.id ASC") 
    , @NamedQuery(name = "Conciliacion.findAllTreeById", query = "SELECT DISTINCT(c) FROM Conciliacion c LEFT JOIN FETCH c.escenarios e WHERE c.id = :idConciliacion")  
    , @NamedQuery(name = "Conciliacion.findByCodConciliacion", query = "SELECT c FROM Conciliacion c WHERE c.id = :codConciliacion")
    , @NamedQuery(name = "Conciliacion.findByCamposTablaDestino", query = "SELECT c FROM Conciliacion c WHERE c.camposTablaDestino = :camposTablaDestino")
    , @NamedQuery(name = "Conciliacion.findByDescripcion", query = "SELECT c FROM Conciliacion c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Conciliacion.findByFechaActualizacion", query = "SELECT c FROM Conciliacion c WHERE c.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Conciliacion.findByFechaCreacion", query = "SELECT c FROM Conciliacion c WHERE c.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Conciliacion.findByNombreConciliacion", query = "SELECT c FROM Conciliacion c WHERE c.nombre = :nombreConciliacion")
    , @NamedQuery(name = "Conciliacion.findByTablaDestino", query = "SELECT c FROM Conciliacion c WHERE c.tablaDestino = :tablaDestino")
    , @NamedQuery(name = "Conciliacion.findByUsuario", query = "SELECT c FROM Conciliacion c WHERE c.usuario = :usuario")
    , @NamedQuery(name = "Conciliacion.findByPoliticaNull", query = "SELECT c FROM Conciliacion c WHERE c.politica IS null")
    , @NamedQuery(name = "Conciliacion.findByPolitica", query = "SELECT c FROM Conciliacion c WHERE c.politica.id = :codPolitica")
    , @NamedQuery(name = "Conciliacion.findByAnyColumn", query = "SELECT DISTINCT(c) FROM Conciliacion c WHERE lower(c.nombre) LIKE lower(:nombreConciliacion) or lower(c.descripcion) LIKE lower(:descripcion) or LOWER(c.politica.nombre) LIKE lower(:nombrePolitica)")})
    
public class Conciliacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_CONCILIACION")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
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
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "COD_POLITICA")
    private Politica politica;
    
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.EAGER, mappedBy = "conciliacion", orphanRemoval = true)
    private Set<Escenario> escenarios;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conciliacion")
    private Collection<WsTransformacion> wsTransformacionesCollection;

    
    public Conciliacion() {
    }

    public Conciliacion(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @XmlTransient
    public Set<Escenario> getEscenarios() {
        return escenarios;
    }

    public void setEscenarios(Set<Escenario> escenarios) {
        this.escenarios = escenarios;
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
        if (!(object instanceof Conciliacion)) {
            return false;
        }
        Conciliacion other = (Conciliacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.Conciliacion[ codConciliacion=" + id + "  ]";
    }
    
    public ConciliacionDTO toDTO(){
        ConciliacionDTO entidadDTO = new ConciliacionDTO();
        
        //Campos comunes
        entidadDTO.setId(id);
        entidadDTO.setFechaCreacion(fechaCreacion);
        entidadDTO.setFechaActualizacion(fechaActualizacion);
        entidadDTO.setNombre(nombre);

        //Campos de la entidad
        entidadDTO.setDescripcion(descripcion);
        entidadDTO.setCamposTablaDestino(camposTablaDestino);
        entidadDTO.setTablaDestino(tablaDestino);
        entidadDTO.setUsuario(usuario);
        entidadDTO.setIdPolitica(politica != null ? politica.getId() : null);
        if (escenarios != null) {
            List<EscenarioDTO> lstEscenarios = escenarios.stream().map((escenarioDTO) -> escenarioDTO.toDTO()).collect(toList());
            entidadDTO.setEscenarios(lstEscenarios);
        }

        //Campos padre
        entidadDTO.setIdPolitica(politica != null ? politica.getId() : null);
        entidadDTO.setNombrePolitica(politica != null ? politica.getNombre() : null);        
        return entidadDTO;
    }

    @XmlTransient
    @org.codehaus.jackson.annotate.JsonIgnore
    public Collection<WsTransformacion> getWsTransformacionesCollection() {
        return wsTransformacionesCollection;
    }

    public void setWsTransformacionesCollection(Collection<WsTransformacion> wsTransformacionesCollection) {
        this.wsTransformacionesCollection = wsTransformacionesCollection;
    }
    
}
