/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto.parent;

import co.com.claro.model.entity.Conciliacion;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para Conciliacion
 * @author andres
 */
@XmlRootElement
public class ConciliacionTreeDTO extends PadreDTO implements Serializable{
    private String tablaDestino;
    private String camposTablaDestino;
    private String descripcion;
    private List<EscenarioTreeDTO> escenarios;

    public List<EscenarioTreeDTO> getEscenarios() {
        return escenarios;
    }

    public void setEscenarios(List<EscenarioTreeDTO> escenarios) {
        this.escenarios = escenarios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTablaDestino() {
        return tablaDestino;
    }

    public void setTablaDestino(String tablaDestino) {
        this.tablaDestino = tablaDestino;
    }
    
    public String getCamposTablaDestino() {
        return camposTablaDestino;
    }

    public void setCamposTablaDestino(String camposTablaDestino) {
        this.camposTablaDestino = camposTablaDestino;
    }
    
    public Conciliacion toEntity(){
        Conciliacion p = new Conciliacion();
        //Campos Comunes
        p.setId(this.getId());
        p.setNombre(this.getNombre());
        p.setUsuario(this.getUsuario());
        p.setFechaCreacion(this.getFechaCreacion());
        p.setFechaActualizacion(this.getFechaActualizacion());
        
        //Campos de la entidad
        p.setDescripcion(this.descripcion);
        p.setTablaDestino(this.tablaDestino);
        p.setCamposTablaDestino(this.camposTablaDestino);
        return p;
        
    }
}
