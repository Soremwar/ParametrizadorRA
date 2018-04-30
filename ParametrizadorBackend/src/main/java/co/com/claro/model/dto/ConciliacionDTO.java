/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andres
 */
@XmlRootElement
public class ConciliacionDTO extends PadreDTO implements Serializable{
    private String tablaDestino;
    private String camposTablaDestino;
    private String descripcion;

    private PoliticaDTO politica;
    

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
    
    public PoliticaDTO getPolitica() {
        return politica;
    }

    public void setPolitica(PoliticaDTO politica) {
        this.politica = politica;
    }

    public Conciliacion toEntity(){
        Conciliacion p = new Conciliacion();
        //campos padre
        p.setId(this.getId());
        p.setNombre(this.getNombre());
        p.setUsuario(this.getUsuario());
        p.setFechaCreacion(this.getFechaCreacion());
        p.setFechaActualizacion(this.getFechaActualizacion());
        
        //campos propios de la entidad
        p.setDescripcion(this.descripcion);
        p.setTablaDestino(this.tablaDestino);
        p.setCamposTablaDestino(this.camposTablaDestino);
        if (this.politica != null) {
            p.setPolitica(this.politica.toEntity());
        }
        return p;
        
    }
}
