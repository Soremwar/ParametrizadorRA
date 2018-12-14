package co.com.claro.model.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "TBL_GAI_USUARIO_ROL")
@XmlRootElement
@NamedQueries({  
	 @NamedQuery(name = "UsuarioRol.findRol", query = "SELECT u FROM UsuarioRol u WHERE u.iduser = :id")
    ,@NamedQuery(name = "UsuarioRol.deleteRol", query = "DELETE FROM UsuarioRol u WHERE u.iduser = :id")})

public class UsuarioRol implements Serializable{
	
	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_USUARIO_ROL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "COD_USUARIO")
    private Integer iduser;
    
    @Basic(optional = false)
    @Column(name = "COD_ROL")
    private Integer idrol;
    
    public UsuarioRol() {
    }

    public UsuarioRol(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }
    
    public Integer getIdrol() {
        return idrol;
    }

    public void setIdrol(Integer idrol) {
        this.idrol = idrol;
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
        if (!(object instanceof UsuarioRol)) {
            return false;
        }
        UsuarioRol other = (UsuarioRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.UsuarioRol[ codUsuarioRol=" + id + " ]";
    }

}
