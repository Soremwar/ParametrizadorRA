/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author andresbedoya
 */
@Embeddable
public class QueryEscenarioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_QUERY_ESCENARIO")
    private int codQueryEscenario;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_ESCENARIO")
    private int codEscenario;

    public QueryEscenarioPK() {
    }

    public QueryEscenarioPK(int codQueryEscenario, int codEscenario) {
        this.codQueryEscenario = codQueryEscenario;
        this.codEscenario = codEscenario;
    }

    public int getCodQueryEscenario() {
        return codQueryEscenario;
    }

    public void setCodQueryEscenario(int codQueryEscenario) {
        this.codQueryEscenario = codQueryEscenario;
    }

    public int getCodEscenario() {
        return codEscenario;
    }

    public void setCodEscenario(int codEscenario) {
        this.codEscenario = codEscenario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codQueryEscenario;
        hash += (int) codEscenario;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QueryEscenarioPK)) {
            return false;
        }
        QueryEscenarioPK other = (QueryEscenarioPK) object;
        if (this.codQueryEscenario != other.codQueryEscenario) {
            return false;
        }
        if (this.codEscenario != other.codEscenario) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.ejb.dao.QueryEscenarioPK[ codQueryEscenario=" + codQueryEscenario + ", codEscenario=" + codEscenario + " ]";
    }
    
}
