/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Esta clase maneja todos los mensajes de error en un formato estandar
 * @author andres bedoya
 */

@XmlRootElement
public class WrapperResponseEntity implements Serializable{
    private int codigo;
    private String mensaje;
    private String constraint;
    private String descripcion; 
    
    @JsonIgnore
    private int httpStatus;

    public WrapperResponseEntity() {
        
    }
    
    public WrapperResponseEntity(HttpCodeType httpCode, String mensaje, String descripcion) {
        this.codigo = httpCode.getCode();
        if (mensaje != null && !mensaje.isEmpty()) {
            this.mensaje = mensaje;
        } else {
            this.mensaje = httpCode.getMsg();
        }
        this.descripcion = descripcion;
        this.httpStatus = httpCode.getCode();
    }
    
    public WrapperResponseEntity(Integer codigo, String mensaje, String descripcion) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.descripcion = descripcion;
    }
    
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer code) {
        this.codigo = code;
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

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }
    
    
    public int httpStatus(){
        return httpStatus;
    }

}
