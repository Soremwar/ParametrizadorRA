/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.WsTransformacionDTO;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TBL_GAI_WS_TRANSFORMACIONES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WsTransformacion.findAll", query = "SELECT w FROM WsTransformacion w")
    , @NamedQuery(name = "WsTransformacion.findByCodWs", query = "SELECT w FROM WsTransformacion w WHERE w.codWs = :codWs")
    , @NamedQuery(name = "WsTransformacion.findByNombreWs", query = "SELECT w FROM WsTransformacion w WHERE w.nombreWs = :nombreWs")
    , @NamedQuery(name = "WsTransformacion.findByPaqueteWs", query = "SELECT w FROM WsTransformacion w WHERE w.paqueteWs = :paqueteWs")
    , @NamedQuery(name = "WsTransformacion.findByFechaCreacion", query = "SELECT w FROM WsTransformacion w WHERE w.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "WsTransformacion.findByFechaActualizacion", query = "SELECT w FROM WsTransformacion w WHERE w.fechaActualizacion = :fechaActualizacion")
    , @NamedQuery(name = "WsTransformacion.findByUsuario", query = "SELECT w FROM WsTransformacion w WHERE w.usuario = :usuario")})
public class WsTransformacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_WS")
    private Integer codWs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "NOMBRE_WS")
    private String nombreWs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "PAQUETE_WS")
    private String paqueteWs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "FECHA_ACTUALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "USUARIO")
    private String usuario;
    @JoinColumn(name = "COD_CONCILIACION", referencedColumnName = "COD_CONCILIACION")
    @ManyToOne(optional = false)
    private Conciliacion conciliacion;

    public WsTransformacion() {
    }

    public WsTransformacion(Integer codWs) {
        this.codWs = codWs;
    }

    public WsTransformacion(Integer codWs, String nombreWs, String paqueteWs, Date fechaCreacion, String usuario) {
        this.codWs = codWs;
        this.nombreWs = nombreWs;
        this.paqueteWs = paqueteWs;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

    public Integer getCodWs() {
        return codWs;
    }

    public void setCodWs(Integer codWs) {
        this.codWs = codWs;
    }

    public String getNombreWs() {
        return nombreWs;
    }

    public void setNombreWs(String nombreWs) {
        this.nombreWs = nombreWs;
    }

    public String getPaqueteWs() {
        return paqueteWs;
    }

    public void setPaqueteWs(String paqueteWs) {
        this.paqueteWs = paqueteWs;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Conciliacion getConciliacion() {
        return conciliacion;
    }

    public void setConciliacion(Conciliacion conciliacion) {
        this.conciliacion = conciliacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codWs != null ? codWs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WsTransformacion)) {
            return false;
        }
        WsTransformacion other = (WsTransformacion) object;
        if ((this.codWs == null && other.codWs != null) || (this.codWs != null && !this.codWs.equals(other.codWs))) {
            return false;
        }
        return true;
    }

    public WsTransformacionDTO toDTO() {
        WsTransformacionDTO entidadDTO = new WsTransformacionDTO();
        entidadDTO.setCodWs(this.codWs);
        entidadDTO.setFechaActualizacion(this.fechaActualizacion);
        entidadDTO.setFechaCreacion(this.fechaCreacion);
        entidadDTO.setNombreWs(nombreWs);
        entidadDTO.setPaqueteWs(paqueteWs);
        entidadDTO.setUsuario(usuario);
        
        entidadDTO.setIdConciliacion(conciliacion.getId());
        entidadDTO.setNombreConciliacion(conciliacion.getNombre());
        return entidadDTO;
        
    }
    
    
    @Override
    public String toString() {
        return "co.com.claro.model.entity.WsTransformaciones[ codWs=" + codWs + " ]";
    }
    
}
