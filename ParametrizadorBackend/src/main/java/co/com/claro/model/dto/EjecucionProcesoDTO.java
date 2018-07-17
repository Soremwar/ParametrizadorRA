/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.EjecucionProceso;
import java.io.Serializable;
import java.util.Date;
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
    
    private Integer idConciliacion;
    private String nombreConciliacion;

    private Integer idEscenario;
    private String nombreEscenario;
    
    private Integer estadoEjecucion;
    private String componenteEjecutado;
    private String idPlanInstance;
    private Date fechaEjecucion;
    private Date fechaEjecucionExitosa;
    
    private List<ParametroEscenarioDTO> parametros;


    public Integer getIdConciliacion() {
        return idConciliacion;
    }

    public void setIdConciliacion(Integer idConciliacion) {
        this.idConciliacion = idConciliacion;
    }

    public String getNombreConciliacion() {
        return nombreConciliacion;
    }

    public void setNombreConciliacion(String nombreConciliacion) {
        this.nombreConciliacion = nombreConciliacion;
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

    public Integer getEstadoEjecucion() {
        return estadoEjecucion;
    }

    public void setEstadoEjecucion(Integer estadoEjecucion) {
        this.estadoEjecucion = estadoEjecucion;
    }

    public String getComponenteEjecutado() {
        return componenteEjecutado;
    }

    public void setComponenteEjecutado(String componenteEjecutado) {
        this.componenteEjecutado = componenteEjecutado;
    }

    public String getIdPlanInstance() {
        return idPlanInstance;
    }

    public void setIdPlanInstance(String idPlanInstance) {
        this.idPlanInstance = idPlanInstance;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public Date getFechaEjecucionExitosa() {
        return fechaEjecucionExitosa;
    }

    public void setFechaEjecucionExitosa(Date fechaEjecucionExitosa) {
        this.fechaEjecucionExitosa = fechaEjecucionExitosa;
    }

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

    public List<ParametroEscenarioDTO> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametroEscenarioDTO> parametros) {
        this.parametros = parametros;
    }

    
    
    public EjecucionProceso toEntity(){
        EjecucionProceso p = new EjecucionProceso();
        //Campos Entidad
        p.setId(id);
        p.setNombre(nombre);
        p.setCodEscenario(idEscenario);
        p.setComponenteEjecutado(componenteEjecutado);
        p.setEstadoEjecucion(estadoEjecucion);
        p.setFechaEjecucion(fechaEjecucion);
        p.setFechaEjecucionExitosa(fechaEjecucionExitosa);
        p.setIdPlanInstance(idPlanInstance);
        p.setNombreConciliacion(nombreConciliacion);
        p.setNombreEscenario(nombreEscenario);
        
        return p;
        
    }
}
