/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Politica;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres
 */
@XmlRootElement
@JsonPropertyOrder({ "id", "nombre", "usuario", "fechaCreacion", "fechaActualizacion","objetivo", "descripcion"})
public class PoliticaDTO extends PadreDTO implements Serializable{

    private String objetivo;
    private String descripcion;


    //private Collection<Conciliacion> conciliacion;


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
    
    /*public Collection<Conciliacion> getConciliacion() {
        return conciliacion;
    }

    public void setConciliacion(Collection<Conciliacion> conciliacion) {
        this.conciliacion = conciliacion;
    }*/
    
    public Politica toEntity(){
        Politica p = new Politica();
        p.setId(this.getId());
        p.setNombre(this.getNombre());
        p.setDescripcion(this.getDescripcion());
        p.setObjetivo(this.objetivo);
        p.setDescripcion(this.descripcion);
        p.setUsuario(this.getUsuario());
        p.setFechaCreacion(this.getFechaCreacion());
        p.setFechaActualizacion(this.getFechaActualizacion());
        return p;
        
    }
}
