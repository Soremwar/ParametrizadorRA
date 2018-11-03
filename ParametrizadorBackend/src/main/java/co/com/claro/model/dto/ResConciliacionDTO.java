/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import static co.com.claro.model.entity.Conciliacion_.usuarioAsignado;
import co.com.claro.model.entity.ResConciliacion;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para Conciliacion
 * @author andres
 */
@XmlRootElement
public class ResConciliacionDTO extends PadreDTO implements Serializable{
    //campos padre
    private Long idEjecucion;
    private Date fecInicio;
    private Date fecFin;
    private String estado;
    private BigInteger valInconsistencias;
    private BigInteger valReincidencias;
    private BigInteger valInconsistenciasMesAnt;
    private Double valBeneficio;
    private BigInteger valPqr;
    private String codConciliacion;
    private String codEscenario;

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
    
 
    
    public ResConciliacion toEntity(){
        ResConciliacion entity = new ResConciliacion();
        entity.setIdEjecucion(idEjecucion);
        entity.setEstado(estado);
        entity.setFecFin(fecFin);
        entity.setFecInicio(fecInicio);
        entity.setValBeneficio(valBeneficio);
        entity.setValInconsistencias(valInconsistencias);
        entity.setValInconsistenciasMesAnt(valInconsistenciasMesAnt);
        entity.setValPqr(valPqr);
        entity.setValReincidencias(valReincidencias);
        entity.setCodConciliacion(codConciliacion);
        entity.setCodEscenario(codEscenario);
        return entity;
        
    }

    
}
