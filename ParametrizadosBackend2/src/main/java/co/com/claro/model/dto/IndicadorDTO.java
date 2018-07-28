/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.Indicador;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para escenario
 * @author andres bedoya
 */
@XmlRootElement
public class IndicadorDTO implements Serializable{

    //Campos padre
    private Integer idEscenario;
    private String nombreEscenario;
    
    //Campos entidad
    private Integer id;
    private String nombreFormula;
    private String descripcion;
    private String textoFormula;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreFormula() {
        return nombreFormula;
    }

    public void setNombreFormula(String nombreFormula) {
        this.nombreFormula = nombreFormula;
    }

    public String getTextoFormula() {
        return textoFormula;
    }

    public void setTextoFormula(String textoFormula) {
        this.textoFormula = textoFormula;
    }

    public Integer getIdEscenario() {
        return idEscenario;
    }

    public void setIdEscenario(Integer idEscenario) {
        this.idEscenario = idEscenario;
    }

    public String getNombreEscenario() {
        return nombreEscenario;
    }

    public void setNombreEscenario(String nombreEscenario) {
        this.nombreEscenario = nombreEscenario;
    }
    public Indicador toEntity(){
        Indicador p = new Indicador();
        //Campos Entidad
        p.setId(this.getId());
        p.setNombreFormula(this.nombreFormula);
        p.setDescripcion(this.getDescripcion());
        p.setTextoFormula(this.getTextoFormula());
       

        return p;
        
    }
}
