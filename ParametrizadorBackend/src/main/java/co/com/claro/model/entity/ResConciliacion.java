/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ResConciliacionDTO;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "TBL_GAI_RES_CONCILIACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResConciliacion.findAll", query = "SELECT r FROM ResConciliacion r")
    , @NamedQuery(name = "ResConciliacion.findByIdEjecucion", query = "SELECT r FROM ResConciliacion r WHERE r.idEjecucion = :idEjecucion")
    , @NamedQuery(name = "ResConciliacion.findByFecInicio", query = "SELECT r FROM ResConciliacion r WHERE r.fecInicio = :fecInicio")
    , @NamedQuery(name = "ResConciliacion.findByFecFin", query = "SELECT r FROM ResConciliacion r WHERE r.fecFin = :fecFin")
    , @NamedQuery(name = "ResConciliacion.findByEstado", query = "SELECT r FROM ResConciliacion r WHERE r.estado = :estado")
    , @NamedQuery(name = "ResConciliacion.findByValInconsistencias", query = "SELECT r FROM ResConciliacion r WHERE r.valInconsistencias = :valInconsistencias")
    , @NamedQuery(name = "ResConciliacion.findByValReincidencias", query = "SELECT r FROM ResConciliacion r WHERE r.valReincidencias = :valReincidencias")
    , @NamedQuery(name = "ResConciliacion.findByValInconsistenciasMesAnt", query = "SELECT r FROM ResConciliacion r WHERE r.valInconsistenciasMesAnt = :valInconsistenciasMesAnt")
    , @NamedQuery(name = "ResConciliacion.findByValBeneficio", query = "SELECT r FROM ResConciliacion r WHERE r.valBeneficio = :valBeneficio")
    , @NamedQuery(name = "ResConciliacion.findByValPqr", query = "SELECT r FROM ResConciliacion r WHERE r.valPqr = :valPqr")
    , @NamedQuery(name = "ResConciliacion.findByCodConciliacion", query = "SELECT r FROM ResConciliacion r WHERE r.codConciliacion = :codConciliacion")
    , @NamedQuery(name = "ResConciliacion.findByCodEscenario", query = "SELECT r FROM ResConciliacion r WHERE r.codEscenario = :codEscenario")
    , @NamedQuery(name = "ResConciliacion.findByAnyColumn", query = "SELECT DISTINCT(r) FROM ResConciliacion r WHERE lower(r.idEjecucion) LIKE lower(:idEjecucion) or lower(r.estado) LIKE lower(:estado) or lower(r.codConciliacion) LIKE lower(:codConciliacion) or lower(r.codEscenario) LIKE lower(:codEscenario)")})

public class ResConciliacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_EJECUCION")
    private Long idEjecucion;
    
    @Column(name = "FEC_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecInicio;
    
    @Column(name = "FEC_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecFin;
    
    @Column(name = "FEC_CARGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecCarga;
    
    @Column(name = "FEC_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecActualizacion;
    
    @Size(max = 30)
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "VAL_INCONSISTENCIAS")
    private BigInteger valInconsistencias;
    @Column(name = "VAL_REINCIDENCIAS")
    private BigInteger valReincidencias;
    @Column(name = "VAL_INCONSISTENCIAS_MES_ANT")
    private BigInteger valInconsistenciasMesAnt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VAL_BENEFICIO")
    private Double valBeneficio;
    @Column(name = "VAL_PQR")
    private BigInteger valPqr;
    @Size(max = 20)
    @Column(name = "COD_CONCILIACION")
    private String codConciliacion;
    @Size(max = 20)
    @Column(name = "COD_ESCENARIO")
    private String codEscenario;

    public ResConciliacion() {
    }

    public ResConciliacion(Long idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecCarga() {
        return fecCarga;
    }

    public void setFecCarga(Date fecCarga) {
        this.fecCarga = fecCarga;
    }

    public Date getFecActualizacion() {
        return fecActualizacion;
    }

    public void setFecActualizacion(Date fecActualizacion) {
        this.fecActualizacion = fecActualizacion;
    }

    public Long getIdEjecucion() {
        return idEjecucion;
    }

    public void setIdEjecucion(Long idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public Date getFecInicio() {
        return fecInicio;
    }

    public void setFecInicio(Date fecInicio) {
        this.fecInicio = fecInicio;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigInteger getValInconsistencias() {
        return valInconsistencias;
    }

    public void setValInconsistencias(BigInteger valInconsistencias) {
        this.valInconsistencias = valInconsistencias;
    }

    public BigInteger getValReincidencias() {
        return valReincidencias;
    }

    public void setValReincidencias(BigInteger valReincidencias) {
        this.valReincidencias = valReincidencias;
    }

    public BigInteger getValInconsistenciasMesAnt() {
        return valInconsistenciasMesAnt;
    }

    public void setValInconsistenciasMesAnt(BigInteger valInconsistenciasMesAnt) {
        this.valInconsistenciasMesAnt = valInconsistenciasMesAnt;
    }

    public Double getValBeneficio() {
        return valBeneficio;
    }

    public void setValBeneficio(Double valBeneficio) {
        this.valBeneficio = valBeneficio;
    }

    public BigInteger getValPqr() {
        return valPqr;
    }

    public void setValPqr(BigInteger valPqr) {
        this.valPqr = valPqr;
    }

    public String getCodConciliacion() {
        return codConciliacion;
    }

    public void setCodConciliacion(String codConciliacion) {
        this.codConciliacion = codConciliacion;
    }

    public String getCodEscenario() {
        return codEscenario;
    }

    public void setCodEscenario(String codEscenario) {
        this.codEscenario = codEscenario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEjecucion != null ? idEjecucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResConciliacion)) {
            return false;
        }
        ResConciliacion other = (ResConciliacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public ResConciliacionDTO toDTO() {
        ResConciliacionDTO entidadDTO = new ResConciliacionDTO();
        entidadDTO.setId(id);
        entidadDTO.setIdEjecucion(idEjecucion);
        entidadDTO.setEstado(estado);
        entidadDTO.setFecFin(fecFin);
        entidadDTO.setFecInicio(fecInicio);
        entidadDTO.setFecCarga(fecCarga);
        entidadDTO.setFecActualizacion(fecActualizacion);
        entidadDTO.setValBeneficio(valBeneficio);
        entidadDTO.setValInconsistencias(valInconsistencias);
        entidadDTO.setValInconsistenciasMesAnt(valInconsistenciasMesAnt);
        entidadDTO.setValPqr(valPqr);
        entidadDTO.setValReincidencias(valReincidencias);
        entidadDTO.setCodConciliacion(codConciliacion);
        entidadDTO.setCodEscenario(codEscenario);
        return entidadDTO;
    }

    @Override
    public String toString() {
        return "co.com.claro.model.entity.ResConciliacion[ idEjecucion=" + idEjecucion + " ]";
    }

}
