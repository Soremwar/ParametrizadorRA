/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.EjecucionProcesoDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
@Table(name = "TBL_GAI_LOG_EJECUCION_PROCESOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EjecucionProceso.findAll", query = "SELECT l FROM EjecucionProceso l")
    , @NamedQuery(name = "EjecucionProceso.findByCodEjecucion", query = "SELECT l FROM EjecucionProceso l WHERE l.id = :codEjecucion")
    , @NamedQuery(name = "EjecucionProceso.findByNombre", query = "SELECT l FROM EjecucionProceso l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "EjecucionProceso.findByCodEscenario", query = "SELECT l FROM EjecucionProceso l WHERE l.codEscenario = :codEscenario")
    , @NamedQuery(name = "EjecucionProceso.findByNombreEscenario", query = "SELECT l FROM EjecucionProceso l WHERE l.nombreEscenario = :nombreEscenario")
    , @NamedQuery(name = "EjecucionProceso.findByCodConciliacion", query = "SELECT l FROM EjecucionProceso l WHERE l.conciliacion = :codConciliacion")
    , @NamedQuery(name = "EjecucionProceso.findByNombreConciliacion", query = "SELECT l FROM EjecucionProceso l WHERE l.nombreConciliacion = :nombreConciliacion")
    , @NamedQuery(name = "EjecucionProceso.findByIdPlanInstance", query = "SELECT l FROM EjecucionProceso l WHERE l.idPlanInstance = :idPlanInstance")
    , @NamedQuery(name = "EjecucionProceso.findByEstadoEjecucion", query = "SELECT l FROM EjecucionProceso l WHERE l.estadoEjecucion = :estadoEjecucion")
    , @NamedQuery(name = "EjecucionProceso.findByFechaEjecucion", query = "SELECT l FROM EjecucionProceso l WHERE l.fechaEjecucion = :fechaEjecucion")
    , @NamedQuery(name = "EjecucionProceso.findByFechaEjecucionExitosa", query = "SELECT l FROM EjecucionProceso l WHERE l.fechaEjecucionExitosa = :fechaEjecucionExitosa")
    , @NamedQuery(name = "EjecucionProceso.findByComponenteEjecutado", query = "SELECT l FROM EjecucionProceso l WHERE l.componenteEjecutado = :componenteEjecutado")})
@Cacheable(false)
public class EjecucionProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "COD_EJECUCION")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Size(max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
  
    @Column(name = "COD_ESCENARIO")
    private Integer codEscenario;
    
    @Size(max = 200)
    @Column(name = "NOMBRE_ESCENARIO")
    private String nombreEscenario;
    
    @Size(max = 200)
    @Column(name = "ID_PLAN_INSTANCE")
    private String idPlanInstance;
    
    @Size(max = 200)
    @Column(name = "NOMBRE_CONCILIACION")
    private String nombreConciliacion;
    
    @Column(name = "ESTADO_EJECUCION")
    private Integer estadoEjecucion;
    
    @Column(name = "FECHA_EJECUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEjecucion;
    
    @Column(name = "FECHA_EJECUCION_EXITOSA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEjecucionExitosa;
    
    @Column(name = "COMPONENTE_EJECUTADO")
    @Size(max = 200)
    private String componenteEjecutado;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_CONCILIACION", referencedColumnName = "COD_CONCILIACION")
    private Conciliacion conciliacion;
    
    public EjecucionProceso() {
    }

    public EjecucionProceso(Integer codEjecucion) {
        this.id = codEjecucion;
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

    public Integer getCodEscenario() {
        return codEscenario;
    }

    public void setCodEscenario(Integer codEscenario) {
        this.codEscenario = codEscenario;
    }

    public String getNombreEscenario() {
        return nombreEscenario;
    }

    public void setNombreEscenario(String nombreEscenario) {
        this.nombreEscenario = nombreEscenario;
    }

    public String getNombreConciliacion() {
        return nombreConciliacion;
    }

    public void setNombreConciliacion(String nombreConciliacion) {
        this.nombreConciliacion = nombreConciliacion;
    }

    public Integer getEstadoEjecucion() {
        return estadoEjecucion;
    }

    public void setEstadoEjecucion(Integer estadoEjecucion) {
        this.estadoEjecucion = estadoEjecucion;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion != null ? fechaEjecucion : Date.from(Instant.now());
    }

    public Date getFechaEjecucionExitosa() {
        return fechaEjecucionExitosa;
    }

    public void setFechaEjecucionExitosa(Date fechaEjecucionExitosa) {
        this.fechaEjecucionExitosa = fechaEjecucionExitosa;
    }

    public String getComponenteEjecutado() {
        return componenteEjecutado;
    }

    public void setComponenteEjecutado(String componenteEjecutado) {
        this.componenteEjecutado = componenteEjecutado;
    }

    public String getIdPlanInstance() {
        return idPlanInstance;
    }

    public void setIdPlanInstance(String idPlanInstance) {
        this.idPlanInstance = idPlanInstance;
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
        if (!(object instanceof EjecucionProceso)) {
            return false;
        }
        EjecucionProceso other = (EjecucionProceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public EjecucionProcesoDTO toDTO(){
        EjecucionProcesoDTO entidadDTO = new EjecucionProcesoDTO();
        //Campos comunes
        entidadDTO.setId(this.getId());
        
        //Campos de la entidad
        entidadDTO.setNombre(nombre);
        entidadDTO.setComponenteEjecutado(componenteEjecutado);
        entidadDTO.setEstadoEjecucion(estadoEjecucion);
        entidadDTO.setFechaEjecucion(fechaEjecucion);
        entidadDTO.setFechaEjecucionExitosa(fechaEjecucionExitosa);
        entidadDTO.setIdPlanInstance(idPlanInstance);
        
        entidadDTO.setIdConciliacion(conciliacion != null ? conciliacion.getId() : null);
        entidadDTO.setNombreConciliacion(conciliacion != null ? conciliacion.getNombre() : null);
        
        entidadDTO.setIdEscenario(codEscenario);
        entidadDTO.setNombreEscenario(nombreEscenario);
        
        /*if (resultados != null) {
            List<ResultadoDTO> lstEjecucionProceso = resultados.stream().map((resultado) -> resultado.toDTO()).collect(toList());
            entidadDTO.setResultados(lstEjecucionProceso);
        }*/        
        return entidadDTO;
    }    
    
    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.EjecucionProceso[ codEjecucion=" + id + " ]";
    }
    
}
