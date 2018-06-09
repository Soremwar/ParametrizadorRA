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
    private Long idEjecucion;
    private String xml;

    public Integer getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(Integer idResultado) {
        this.idResultado = idResultado;
    }

    public Long getIdEjecucion() {
        return idEjecucion;
    }

    public void setIdEjecucion(Long idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Resultado toEntity(){
        Resultado p = new Resultado();
        p.setCodResultados(this.idResultado);
        p.setCodEjecucion(this.idEjecucion);
        p.setXmlResultado(this.xml);
       
        return p;
    }
}
