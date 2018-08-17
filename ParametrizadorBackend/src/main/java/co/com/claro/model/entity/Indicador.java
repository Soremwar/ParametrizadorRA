/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.IndicadorDTO;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresbedoya
 */
@Entity
@Table(name = "TBL_GAI_INDICADORES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicador.findAll", query = "SELECT i FROM Indicador i")
    , @NamedQuery(name = "Indicador.findByCodIndicador", query = "SELECT i FROM Indicador i WHERE i.id = :codIndicador")
    , @NamedQuery(name = "Indicador.findByNombreFormula", query = "SELECT i FROM Indicador i WHERE i.nombreFormula = :nombreFormula")
    , @NamedQuery(name = "Indicador.findByDescripcion", query = "SELECT i FROM Indicador i WHERE i.descripcion = :descripcion")
    , @NamedQuery(name = "Indicador.findByTextoFormula", query = "SELECT i FROM Indicador i WHERE i.textoFormula = :textoFormula")
    , @NamedQuery(name = "Indicador.findByAnyColumn", query = "SELECT DISTINCT(i) FROM Indicador i WHERE lower(i.nombreFormula) LIKE lower(:nombre) or lower(i.descripcion) LIKE lower(:descripcion) or LOWER(i.textoFormula) LIKE lower(:textoFormula) or LOWER(i.escenario.nombre) LIKE lower(:nombreescenario)")})
public class Indicador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_INDICADOR")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Size(max = 200)
    @Column(name = "NOMBRE_FORMULA")
    private String nombreFormula;
    
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Size(max = 500)
    @Column(name = "TEXTO_FORMULA")
    private String textoFormula;
    
    @JoinColumn(name = "COD_ESCENARIO", referencedColumnName = "COD_ESCENARIO")
    @ManyToOne
    private Escenario escenario;

    public Indicador() {
    }

    public Indicador(Integer codIndicador) {
        this.id = codIndicador;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreFormula() {
        return nombreFormula;
    }

    public void setNombreFormula(String nombreFormula) {
        this.nombreFormula = nombreFormula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTextoFormula() {
        return textoFormula;
    }

    public void setTextoFormula(String textoFormula) {
        this.textoFormula = textoFormula;
    }

    public Escenario getEscenario() {
        return escenario;
    }

    public void setEscenario(Escenario escenario) {
        this.escenario = escenario;
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
        if (!(object instanceof Indicador)) {
            return false;
        }
        Indicador other = (Indicador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.Indicador[ codIndicador=" + id + " ]";
    }
    
    public IndicadorDTO toDTO(){
        IndicadorDTO entidadDTO = new IndicadorDTO();
        //Campos comunes
        entidadDTO.setId(this.getId());
        
        //Campos de la entidad
        entidadDTO.setNombreFormula(nombreFormula);
        entidadDTO.setDescripcion(descripcion);
        entidadDTO.setTextoFormula(textoFormula);
        entidadDTO.setIdEscenario(escenario != null ? escenario.getId() : null);
        entidadDTO.setNombreEscenario(escenario != null ? escenario.getNombre() : null);
        
        return entidadDTO;
    }
    
}
