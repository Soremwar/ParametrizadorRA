/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Conciliacion;
import co.com.claro.model.entity.Politica;
import java.io.Serializable;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para Conciliacion
 * @author andres
 */
@XmlRootElement
public class ConciliacionDTO extends PadreDTO implements Serializable{
    //campos padre
    private Integer idPolitica;
    private String nombrePolitica;
    
    private String tablaDestino;
    private String camposTablaDestino;
    private String descripcion;
    private String usuarioAsignado;
    
    private Set<EscenarioDTO> escenarios;
    private Set<WsTransformacionDTO> transformaciones;
    private Set<EjecucionProcesoDTO> ejecucionesProceso;
    private Set<QueryAprobacionDTO> queryAprobaciones;
   

    public Integer getIdPolitica() {
        return idPolitica;
    }

    public void setIdPolitica(Integer idPolitica) {
        this.idPolitica = idPolitica;
    }

    public String getNombrePolitica() {
        return nombrePolitica;
    }

    public void setNombrePolitica(String nombrePolitica) {
        this.nombrePolitica = nombrePolitica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getUsuarioAsignado() {
        return usuarioAsignado;
    }

    public void setUsuarioAsignado(String usuarioAsignado) {
        this.usuarioAsignado = usuarioAsignado;
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
    
    
    public Set<EscenarioDTO> getEscenarios() {
        return escenarios;
    }

    public void setEscenarios(Set<EscenarioDTO> escenarios) {
        this.escenarios = escenarios;
    }

    public Set<EjecucionProcesoDTO> getEjecucionesProceso() {
        return ejecucionesProceso;
    }

    public void setEjecucionesProceso(Set<EjecucionProcesoDTO> ejecucionesProceso) {
        this.ejecucionesProceso = ejecucionesProceso;
    }

    
    public Set<WsTransformacionDTO> getTransformaciones() {
        return transformaciones;
    }

    public void setTransformaciones(Set<WsTransformacionDTO> transformaciones) {
        this.transformaciones = transformaciones;
    }

    public Set<QueryAprobacionDTO> getQueryAprobaciones() {
        return queryAprobaciones;
    }

    public void setQueryAprobaciones(Set<QueryAprobacionDTO> queryAprobaciones) {
        this.queryAprobaciones = queryAprobaciones;
    }
    
    public Conciliacion toEntity(){
        Conciliacion entity = new Conciliacion();
        
        //Campos Comunes
        entity.setId(this.getId());
        entity.setNombre(this.getNombre());

        entity.setFechaCreacion(this.getFechaCreacion());
        entity.setFechaActualizacion(this.getFechaActualizacion());
        
        //Campos de la entidad
        entity.setUsuarioAsignado(usuarioAsignado);
        entity.setDescripcion(this.descripcion);
        entity.setTablaDestino(this.tablaDestino);
        entity.setCamposTablaDestino(this.camposTablaDestino);
        entity.setPolitica(this.idPolitica != null ? new Politica(this.idPolitica) : null);
        return entity;
        
    }

    @Override
    public String toString() {
        return "ConciliacionDTO{" + "idPolitica=" + idPolitica + ", nombrePolitica=" + nombrePolitica + ", usuarioAsignado=" + usuarioAsignado + '}';
    }
    
}
