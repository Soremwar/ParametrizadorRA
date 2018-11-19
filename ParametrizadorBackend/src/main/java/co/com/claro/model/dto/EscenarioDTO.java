/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Escenario;
import java.io.Serializable;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para escenario
 *
 * @author andres
 */
@XmlRootElement
public class EscenarioDTO extends PadreDTO implements Serializable {

    //Campos padre
    private Integer idConciliacion;
    private String nombreConciliacion;

    //Campos propios
    private String impacto;
    private String descripcion;

    private Set<QueryEscenarioDTO> queryescenarios;
    private Set<IndicadorDTO> indicadores;

    public String getNombreConciliacion() {
        return nombreConciliacion;
    }

    public void setNombreConciliacion(String nombreConciliacion) {
        this.nombreConciliacion = nombreConciliacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public Integer getIdConciliacion() {
        return idConciliacion;
    }

    public void setIdConciliacion(Integer idConciliacion) {
        this.idConciliacion = idConciliacion;
    }

    public Set<QueryEscenarioDTO> getQueryescenarios() {
        return queryescenarios;
    }

    public void setQueryescenarios(Set<QueryEscenarioDTO> queryescenarios) {
        this.queryescenarios = queryescenarios;
    }

    public Set<IndicadorDTO> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(Set<IndicadorDTO> indicadores) {
        this.indicadores = indicadores;
    }

    public Escenario toEntity() {
        Escenario entity = new Escenario();
        //Campos comunes
        entity.setId(this.getId());
        entity.setNombre(this.getNombre());

        entity.setFechaCreacion(this.getFechaCreacion());
        entity.setFechaActualizacion(this.getFechaActualizacion());

        //Campos de la entidad
        entity.setDescripcion(descripcion);
        entity.setImpacto(this.impacto);
        //p.setConciliacion(this.idConciliacion != null ? new Conciliacion(this.idConciliacion) : null);
        return entity;

    }

}
