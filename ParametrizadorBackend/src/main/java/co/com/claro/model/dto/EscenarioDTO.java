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
 * @author andres
 */
@XmlRootElement
public class EscenarioDTO extends PadreDTO implements Serializable{

    //Campos padre
    private Integer idConciliacion;
    private String nombreConciliacion;

    //Campos propios
    private String usuarioAsignado;
    private String impacto;
    
   private Set<QueryEscenarioDTO> queryescenarios;
    
    public String getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(String usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
    }

    public String getNombreConciliacion() {
        return nombreConciliacion;
    }

    public void setNombreConciliacion(String nombreConciliacion) {
        this.nombreConciliacion = nombreConciliacion;
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

    
    public Escenario toEntity(){
        Escenario entity = new Escenario();
        //Campos comunes
        entity.setId(this.getId());
        entity.setNombre(this.getNombre());
        entity.setUsuario(this.getUsuario());
        entity.setFechaCreacion(this.getFechaCreacion());
        entity.setFechaActualizacion(this.getFechaActualizacion());

        //Campos de la entidad
        entity.setUsuarioAsignado(usuarioAsignado);
        entity.setImpacto(this.impacto);
        //p.setConciliacion(this.idConciliacion != null ? new Conciliacion(this.idConciliacion) : null);
        return entity;
        
    }
}
