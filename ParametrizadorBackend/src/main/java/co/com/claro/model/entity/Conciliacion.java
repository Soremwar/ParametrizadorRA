/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.EjecucionProcesoDTO;
import co.com.claro.model.dto.EscenarioDTO;
import co.com.claro.model.dto.QueryAprobacionDTO;
import co.com.claro.model.dto.WsTransformacionDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import static java.util.Comparator.comparing;
import java.util.Date;
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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "TBL_GAI_CONCILIACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conciliacion.findAll", query = "SELECT DISTINCT(c) FROM Conciliacion c")
    , @NamedQuery(name = "Conciliacion.findAllTree", query = "SELECT DISTINCT(c) FROM Conciliacion c LEFT JOIN FETCH c.escenarios e ORDER BY c.id ASC") 
    , @NamedQuery(name = "Conciliacion.findAllTreeById", query = "SELECT DISTINCT(c) FROM Conciliacion c LEFT JOIN FETCH c.escenarios e WHERE c.id = :idConciliacion")  
    //, @NamedQuery(name = "Conciliacion.findAllTree", query = "SELECT DISTINCT(c) FROM Conciliacion c ORDER BY c.id ASC") 
    //, @NamedQuery(name = "Conciliacion.findAllTreeById", query = "SELECT DISTINCT(c) FROM Conciliacion c WHERE c.id = :idConciliacion")  
    , @NamedQuery(name = "Conciliacion.findByCodConciliacion", query = "SELECT c FROM Conciliacion c WHERE c.id = :codConciliacion")
    , @NamedQuery(name = "Conciliacion.findByCamposTablaDestino", query = "SELECT c FROM Conciliacion c WHERE c.camposTablaDestino = :camposTablaDestino")
    , @NamedQuery(name = "Conciliacion.findByDescripcion", query = "SELECT c FROM Conciliacion c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Conciliacion.findByFechaActualizacion", query = "SELECT c FROM Conciliacion c WHERE c.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Conciliacion.findByFechaCreacion", query = "SELECT c FROM Conciliacion c WHERE c.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Conciliacion.findByNombreConciliacion", query = "SELECT c FROM Conciliacion c WHERE c.nombre = :nombreConciliacion")
    , @NamedQuery(name = "Conciliacion.findByTablaDestino", query = "SELECT c FROM Conciliacion c WHERE c.tablaDestino = :tablaDestino")
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
    
    @Size(max = 100)
    @Column(name = "NOMBRE_CONCILIACION")
    private String nombre;
    
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Size(max = 50)
    @Column(name = "TABLA_DESTINO")
    private String tablaDestino;
    
    @Size(max = 200)
    @Column(name = "CAMPOS_TABLA_DESTINO")
    private String camposTablaDestino;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
     
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    
    @Size(max = 100)
    @Column(name = "USUARIO_ASIGNADO")
    private String usuarioAsignado;
        
    //@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    //@JoinColumn(name = "COD_POLITICA")
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "COD_POLITICA")
    private Politica politica;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "conciliacion")
    private Collection<Escenario> escenarios;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "conciliacion")
    private Collection<WsTransformacion> transformaciones;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "conciliacion")
    private Collection<EjecucionProceso> ejecucionesProceso;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "conciliacion")
    private Collection<QueryAprobacion> queriesAprobacion;
    
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTablaDestino() {
        return tablaDestino;
    }

    public void setTablaDestino(String tablaDestino) {
        this.tablaDestino = tablaDestino;
    }
    
    public String getUsuarioAsignado() {
        return usuarioAsignado; 
    }

    public void setUsuarioAsignado(String usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }
    
    public Politica getPolitica() {
        return politica;
    }

    public void setPolitica(Politica politica) {
        this.politica = politica;
    }

    public void addEscenario(Escenario escenario) {
        escenarios.add(escenario);
        escenario.setConciliacion(this);
    }
    
    public void removeEscenario(Escenario escenario) {
        this.escenarios.remove(escenario);
        escenario.setConciliacion(null);
    }
    
    public void addTransformacion(WsTransformacion transformacion) {
        this.transformaciones.add(transformacion);
        transformacion.setConciliacion(this);
    }
    
    public void removeTransformacion(WsTransformacion transformacion) {
        this.transformaciones.remove(transformacion);
        transformacion.setConciliacion(null);
    }
    
    public void addEjecucionProceso(EjecucionProceso ejecucionProceso) {
        this.ejecucionesProceso.add(ejecucionProceso);
        ejecucionProceso.setConciliacion(this);
    }
    
    public void removeEjecucionProceso(EjecucionProceso ejecucionProceso) {
        this.ejecucionesProceso.remove(ejecucionProceso);
        ejecucionProceso.setConciliacion(null);
    }
    
    public void addQueryAprobacion(QueryAprobacion queryAprobacion) {
        this.queriesAprobacion.add(queryAprobacion);
        queryAprobacion.setConciliacion(this);
    }
    
    public void removeQueryAprobacion(QueryAprobacion queryAprobacion) {
        this.queriesAprobacion.remove(queryAprobacion);
        queryAprobacion.setConciliacion(null);
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
        return "co.com.claro.ejb.dao.Conciliacion[ id=" + id + "  ]";
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
        entidadDTO.setUsuarioAsignado(usuarioAsignado);
        if (escenarios != null) {
            Set<EscenarioDTO> lstEscenarios = escenarios.stream().map((escenarioDTO) -> escenarioDTO.toDTO()).collect(Collectors.toSet());
            entidadDTO.setEscenarios(lstEscenarios);
        }
        
        if (transformaciones != null) {
            Set<WsTransformacionDTO> lstTransformaciones = transformaciones.stream().map((transformacionDTO) -> transformacionDTO.toDTO()).collect(Collectors.toSet());
            entidadDTO.setTransformaciones(lstTransformaciones);
        }

        if (ejecucionesProceso != null) {
            Set<EjecucionProcesoDTO> lstEjecuciones = ejecucionesProceso.stream().map((ejecucionProcesoDTO) -> ejecucionProcesoDTO.toDTO()).sorted(comparing(EjecucionProcesoDTO::getFechaEjecucion)).collect(Collectors.toSet());
            entidadDTO.setEjecucionesProceso(lstEjecuciones);
        }

        if (queriesAprobacion != null) {
            Set<QueryAprobacionDTO> lstAprobaciones = queriesAprobacion.stream().map((queriesAprobacionDTO) -> queriesAprobacionDTO.toDTO()).sorted(comparing(QueryAprobacionDTO::getFechaCreacion)).collect(Collectors.toSet());
            entidadDTO.setQueryAprobaciones(lstAprobaciones);
        }
        
        //Campos padre
        entidadDTO.setIdPolitica(politica != null ? politica.getId() : null);
        entidadDTO.setNombrePolitica(politica != null ? politica.getNombre() : null);        
        return entidadDTO;
    }

    
}
