/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ResultadoDTO;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andresbedoya
 */
@Entity
@Table(name = "TBL_GAI_RESULTADOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resultados.findAll", query = "SELECT r FROM Resultado r")
    , @NamedQuery(name = "Resultados.findByCodResultados", query = "SELECT r FROM Resultado r WHERE r.id = :codResultados")
    , @NamedQuery(name = "Resultados.findByCodEjecucion", query = "SELECT r FROM Resultado r WHERE r.ejecucion = :codEjecucion")})
@Cacheable(false)
public class Resultado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_RESULTADOS")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "XML_RESULTADO")
    private String xmlResultado;
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_EJECUCION")
    private EjecucionProceso ejecucion;
    

    public Resultado() {
    }

    public Resultado(Integer codResultados) {
        this.id = codResultados;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getXmlResultado() {
        return xmlResultado;
    }

    public void setXmlResultado(String xmlResultado) {
        this.xmlResultado = xmlResultado;
    }

    public EjecucionProceso getEjecucion() {
        return ejecucion;
    }

    public void setEjecucion(EjecucionProceso ejecucion) {
        this.ejecucion = ejecucion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Resultado)) {
            return false;
        }
        Resultado other = (Resultado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    public ResultadoDTO toDTO() {
        ResultadoDTO entidadDTO = new ResultadoDTO();
        //entidadDTO.setIdEjecucion(this.codEjecucion);
        entidadDTO.setIdResultado(this.id);
        entidadDTO.setXml(this.xmlResultado);
        
        //Campos padre
        entidadDTO.setIdEjecucion(ejecucion != null ? ejecucion.getId() : null); 
        entidadDTO.setIdEscenario(ejecucion != null ? ejecucion.getCodEscenario() : null);
        return entidadDTO;
        
    }
    
    
    @Override
    public String toString() {
        return "co.com.claro.model.entity.Resultados[ codResultados=" + id + " ]";
    }
    
}
