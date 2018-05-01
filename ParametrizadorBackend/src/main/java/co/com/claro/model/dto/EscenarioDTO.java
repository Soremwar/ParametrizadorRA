/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.model.dto;

import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.entity.Escenario;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO para escenario
 * @author andres
 */
@XmlRootElement
public class EscenarioDTO extends PadreDTO implements Serializable{

    private String impacto;
    private ConciliacionDTO conciliacion;

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public ConciliacionDTO getConciliacion() {
        return conciliacion;
    }

    public void setConciliacion(ConciliacionDTO conciliacion) {
        this.conciliacion = conciliacion;
    }



    public Escenario toEntity(){
        Escenario p = new Escenario();
        //Campos comunes
        p.setId(this.getId());
        p.setNombre(this.getNombre());
        p.setUsuario(this.getUsuario());
        p.setFechaCreacion(this.getFechaCreacion());
        p.setFechaActualizacion(this.getFechaActualizacion());

        //Campos de la entidad
        p.setImpacto(this.impacto);
        p.setConciliacion(conciliacion != null ? conciliacion.toEntity() : null);
        return p;
        
    }
}
