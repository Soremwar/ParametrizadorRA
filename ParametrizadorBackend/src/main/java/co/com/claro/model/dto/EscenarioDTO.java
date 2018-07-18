/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Escenario;
import java.io.Serializable;
import java.util.List;
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
    
    private List<ParametroEscenarioDTO> parametros;


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

    public List<ParametroEscenarioDTO> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametroEscenarioDTO> parametros) {
        this.parametros = parametros;
    }

    
    public Escenario toEntity(){
        Escenario p = new Escenario();
        //Campos comunes
        p.setId(this.getId());
        p.setNombre(this.getNombre());
        p.setUsuario(this.getUsuario());
        p.setFechaCreacion(this.getFechaCreacion());
        p.setFechaActualizacion(this.getFechaActualizacion());

        //Campos de la entidad
        p.setUsuarioAsignado(usuarioAsignado);
        p.setImpacto(this.impacto);
        //p.setConciliacion(this.idConciliacion != null ? new Conciliacion(this.idConciliacion) : null);
        return p;
        
    }
}
