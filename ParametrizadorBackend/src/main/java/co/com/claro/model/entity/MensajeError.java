/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Esta clase maneja todos los mensajes de error
 * @author andres bedoya
 */

@XmlRootElement
public class MensajeError implements Serializable{
    private int codigo;
    private String mensaje;
    private String descripcion; 

    public MensajeError() {
        
    }
    public MensajeError(int codigo, String mensaje, String descripcion) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.descripcion = descripcion;
    }
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
