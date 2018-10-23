/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.EscenarioDTO;
import co.com.claro.model.dto.IndicadorDTO;
import co.com.claro.model.dto.QueryEscenarioDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresbedoya
 */
@Entity
@Table(name = "TBL_GAI_ESCENARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Escenario.findAll", query = "SELECT DISTINCT(e) FROM Escenario e")
    , @NamedQuery(name = "Escenario.findAllTree", query = "SELECT DISTINCT(e) FROM Escenario e LEFT JOIN FETCH e.conciliacion c")
    , @NamedQuery(name = "Escenario.findByCodEscenario", query = "SELECT e FROM Escenario e WHERE e.id = :codEscenario")
    , @NamedQuery(name = "Escenario.findByNombreEscenario", query = "SELECT e FROM Escenario e WHERE e.nombre = :nombreEscenario")
    , @NamedQuery(name = "Escenario.findByImpacto", query = "SELECT e FROM Escenario e WHERE e.impacto = :impacto")
    , @NamedQuery(name = "Escenario.findByFechaCreacion", query = "SELECT e FROM Escenario e WHERE e.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Escenario.findByFechaActualizacion", query = "SELECT e FROM Escenario e WHERE e.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Escenario.findByConciliacionNull", query = "SELECT e FROM Escenario e WHERE e.conciliacion IS null")
    , @NamedQuery(name = "Escenario.findByConciliacion", query = "SELECT e FROM Escenario e WHERE e.conciliacion = :codConciliacion")
    , @NamedQuery(name = "Escenario.findByAnyColumn", query = "SELECT DISTINCT(e) FROM Escenario e WHERE lower(e.id) LIKE lower(:id) or lower(e.nombre) LIKE lower(:nombreEscenario) or lower(e.impacto) LIKE lower(:impacto) or lower(e.conciliacion.nombre) LIKE lower(:nombreConciliacion)")})
        
public class Escenario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_ESCENARIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "NOMBRE_ESCENARIO")
    private String nombre;
    
    @Column(name = "IMPACTO")
    private String impacto;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
        
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "escenario")
    private List<Indicador> indicadores;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "escenario")
    private List<QueryEscenario> queryEscenarios;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_CONCILIACION")
    private Conciliacion conciliacion;

    public Escenario() {
    }

    public Escenario(Integer id) {
        this.id = id;
    }

    public Escenario(Integer id, String nombreEscenario) {
        this.id = id;
        this.nombre = nombreEscenario;
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
    
    public Conciliacion getConciliacion() {
        return conciliacion;
    }

    public void setConciliacion(Conciliacion conciliacion) {
        this.conciliacion = conciliacion;
    }
    
    public void addIndicador(Indicador indicador) {
        this.indicadores.add(indicador);
        indicador.setEscenario(this);
    }
    
    public void removeIndicador(Indicador indicador) {
        this.indicadores.remove(indicador);
        indicador.setEscenario(null);
    }
    
    public void addQueryEscenario(QueryEscenario queryEscenario) {
        this.queryEscenarios.add(queryEscenario);
        queryEscenario.setEscenario(this);
    }
    
    public void removeIndicador(QueryEscenario queryEscenario) {
        this.queryEscenarios.remove(queryEscenario);
        queryEscenario.setEscenario(null);
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
        return "Escenario{" + "id=" + id + ", nombre=" + nombre + '}';
    }
    
    public EscenarioDTO toDTO(){
        EscenarioDTO entidadDTO = new EscenarioDTO();
        //Campos comunes
        entidadDTO.setId(this.getId());
        entidadDTO.setFechaCreacion(this.getFechaCreacion());
        entidadDTO.setFechaActualizacion(this.getFechaActualizacion());
        entidadDTO.setNombre(this.getNombre());
        
        //Campos de la entidad
        entidadDTO.setImpacto(impacto);
        entidadDTO.setIdConciliacion(conciliacion != null ? conciliacion.getId() : null);
        entidadDTO.setNombreConciliacion(conciliacion != null ? conciliacion.getNombre() : null);
        if (queryEscenarios != null) {
            Set<QueryEscenarioDTO> lstqueryEscenarios = queryEscenarios.stream().map((escenarioDTO) -> escenarioDTO.toDTO()).collect(Collectors.toSet());
            entidadDTO.setQueryescenarios(lstqueryEscenarios);
        }
        
        if (indicadores != null) {
            Set<IndicadorDTO> lstIndicadores = indicadores.stream().map((escenarioDTO) -> escenarioDTO.toDTO()).collect(Collectors.toSet());
            entidadDTO.setIndicadores(lstIndicadores);
        }
        
        return entidadDTO;
    }

}
