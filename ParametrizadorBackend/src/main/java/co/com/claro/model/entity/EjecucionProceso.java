/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.EjecucionProcesoDTO;
import co.com.claro.model.dto.ResultadoDTO;
import static co.com.claro.model.entity.ParametroEscenario_.escenario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
    , @NamedQuery(name = "EjecucionProceso.findByCodConciliacion", query = "SELECT l FROM EjecucionProceso l WHERE l.codConciliacion = :codConciliacion")
    , @NamedQuery(name = "EjecucionProceso.findByNombreConciliacion", query = "SELECT l FROM EjecucionProceso l WHERE l.nombreConciliacion = :nombreConciliacion")
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
    
    @Column(name = "COD_CONCILIACION")
    private Integer codConciliacion;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date componenteEjecutado;

    @OneToMany(fetch=FetchType.LAZY, mappedBy = "ejecucion")
    private List<Resultado> resultados;
    
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
        this.fechaEjecucion = fechaEjecucion;
    }

    public Date getFechaEjecucionExitosa() {
        return fechaEjecucionExitosa;
    }

    public void setFechaEjecucionExitosa(Date fechaEjecucionExitosa) {
        this.fechaEjecucionExitosa = fechaEjecucionExitosa;
    }

    public Date getComponenteEjecutado() {
        return componenteEjecutado;
    }

    public void setComponenteEjecutado(Date componenteEjecutado) {
        this.componenteEjecutado = componenteEjecutado;
    }

    public void addResultado(Resultado resultado) {
        resultados.add(resultado);
        resultado.setEjecucion(this);
    }
    
    public void removeResultado(Resultado resultado) {
        this.resultados.remove(resultado);
        resultado.setEjecucion(null);
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
        if (resultados != null) {
            List<ResultadoDTO> lstEjecucionProceso = resultados.stream().map((resultado) -> resultado.toDTO()).collect(toList());
            entidadDTO.setResultados(lstEjecucionProceso);
        }        
        return entidadDTO;
    }    
    
    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.EjecucionProceso[ codEjecucion=" + id + " ]";
    }
    
}
