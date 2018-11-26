/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.entity.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresbedoya
 */
@XmlRootElement
public class UsuarioDTO implements Serializable {

    private Integer id;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private String usuario;
    private String nombreUsuario;
    private String email;
    private String nombreRol;
    
    private Set<RolDTO> roles;
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public Set<RolDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolDTO> roles) {
        this.roles = roles;
    }
    
    
    public Usuario toEntity() {
        Usuario entity = new Usuario();
        entity.setId(this.getId());
        entity.setUsuario(usuario);
        entity.setFechaCreacion(this.getFechaCreacion());
        entity.setFechaActualizacion(this.getFechaActualizacion());
        entity.setEmail(email);
        entity.setNombreUsuario(nombreUsuario);
        return entity;

    }
}
