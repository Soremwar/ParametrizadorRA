/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import co.com.claro.model.dto.ResultadoDTO;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
    , @NamedQuery(name = "Resultados.findByCodResultados", query = "SELECT r FROM Resultado r WHERE r.codResultados = :codResultados")
    , @NamedQuery(name = "Resultados.findByCodEjecucion", query = "SELECT r FROM Resultado r WHERE r.codEjecucion = :codEjecucion")})
public class Resultado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_RESULTADOS")
    private Integer codResultados;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_EJECUCION")
    private long codEjecucion;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "XML_RESULTADO")
    private String xmlResultado;

    public Resultado() {
    }

    public Resultado(Integer codResultados) {
        this.codResultados = codResultados;
    }

    public Resultado(Integer codResultados, long codEjecucion, String xmlResultado) {
        this.codResultados = codResultados;
        this.codEjecucion = codEjecucion;
        this.xmlResultado = xmlResultado;
    }

    public Integer getCodResultados() {
        return codResultados;
    }

    public void setCodResultados(Integer codResultados) {
        this.codResultados = codResultados;
    }

    public long getCodEjecucion() {
        return codEjecucion;
    }

    public void setCodEjecucion(long codEjecucion) {
        this.codEjecucion = codEjecucion;
    }

    public String getXmlResultado() {
        return xmlResultado;
    }

    public void setXmlResultado(String xmlResultado) {
        this.xmlResultado = xmlResultado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codResultados != null ? codResultados.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Resultado)) {
            return false;
        }
        Resultado other = (Resultado) object;
        if ((this.codResultados == null && other.codResultados != null) || (this.codResultados != null && !this.codResultados.equals(other.codResultados))) {
            return false;
        }
        return true;
    }

    
    public ResultadoDTO toDTO() {
        ResultadoDTO entidadDTO = new ResultadoDTO();
        entidadDTO.setIdEjecucion(this.codEjecucion);
        entidadDTO.setIdResultado(this.codResultados);
        entidadDTO.setXml(this.xmlResultado);
        return entidadDTO;
        
    }
    
    
    @Override
    public String toString() {
        return "co.com.claro.model.entity.Resultados[ codResultados=" + codResultados + " ]";
    }
    
}
