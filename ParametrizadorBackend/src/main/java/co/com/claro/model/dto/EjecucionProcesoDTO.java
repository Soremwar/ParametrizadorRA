/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.EjecucionProceso;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para EjecucionProceso
 * @author andres bedoya
 */
@XmlRootElement
public class EjecucionProcesoDTO implements Serializable{
    //Campos entidad
    private Integer id;
    private String nombre;
    private List<ResultadoDTO> resultados;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<ResultadoDTO> getResultados() {
        return resultados;
    }

    public void setResultados(List<ResultadoDTO> resultados) {
        this.resultados = resultados;
    }

    public EjecucionProceso toEntity(){
        EjecucionProceso p = new EjecucionProceso();
        //Campos Entidad
        p.setId(id);
        p.setNombre(nombre);
        return p;
        
    }
}
