/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.RolDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresbedoya
 */
@Entity
@Table(name = "TBL_GAI_ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r")
    , @NamedQuery(name = "Rol.findByCodRol", query = "SELECT r FROM Rol r WHERE r.id = :id")
    , @NamedQuery(name = "Rol.findByNombre", query = "SELECT r FROM Rol r WHERE r.nombre = :nombre")
    , @NamedQuery(name = "Rol.findByFechaCreacion", query = "SELECT r FROM Rol r WHERE r.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Rol.findByFechaActualizacion", query = "SELECT r FROM Rol r WHERE r.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "Rol.findByMenus", query = "SELECT r FROM Rol r WHERE r.menus = :menus")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_ROL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;

    @Basic(optional = false)
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;

    @Size(max = 500)
    @Column(name = "MENUS")
    private String menus;

    /*@OneToMany(mappedBy = "codRol")
    private Collection<UsuarioRol> usuarioRolCollection;*/
    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios = new HashSet<>();

    public Rol() {
    }

    public Rol(Integer codRol) {
        this.id = codRol;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : Date.from(Instant.now());
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getMenus() {
        return menus;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.Rol[ codRol=" + id + " ]";
    }

    public RolDTO toDTO() {
        RolDTO entidadDTO = new RolDTO();
        entidadDTO.setId(id);
        entidadDTO.setFechaCreacion(fechaCreacion);
        entidadDTO.setFechaActualizacion(fechaActualizacion);
        entidadDTO.setNombre(nombre);
        entidadDTO.setMenus(menus);

        /*if (usuarioRolCollection != null) {            
            Set<ConciliacionDTO> lstConciliaciones = conciliaciones.stream().map((itemDTO) -> itemDTO.toDTO()).sorted(Comparator.comparing(ConciliacionDTO::getId).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
            entidadDTO.setConciliaciones(lstConciliaciones);
        }*/
        return entidadDTO;
    }
}
