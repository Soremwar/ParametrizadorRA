/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.Resultado;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para Resultado
 * @author andres
 */
@XmlRootElement
public class ResultadoDTO implements Serializable{

    private Integer idResultado;
    private Integer idEjecucion;
    private Integer idEscenario;


    private String variables;

    public Integer getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(Integer idResultado) {
        this.idResultado = idResultado;
    }

    public Integer getIdEjecucion() {
        return idEjecucion;
    }

    public void setIdEjecucion(Integer idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Integer getIdEscenario() {
        return idEscenario;
    }

    public void setIdEscenario(Integer idEscenario) {
        this.idEscenario = idEscenario;
    }
    public Resultado toEntity(){
        Resultado p = new Resultado();
        p.setId(this.idResultado);
        p.setXmlResultado(this.variables);
       
        return p;
    }

    @Override
    public String toString() {
        return "ResultadoDTO{" + "idResultado=" + idResultado + ", idEjecucion=" + idEjecucion + ", idEscenario=" + idEscenario + ", variables=" + variables + '}';
    }
    
    
    
}
