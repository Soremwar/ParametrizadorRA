/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao.utils;

import co.com.claro.ejb.dao.utils.comparators.EntityFechaCreacionComparator;
import co.com.claro.ejb.dao.utils.comparators.EntityIdComparator;
import co.com.claro.ejb.dao.utils.comparators.EntityNameComparator;
import co.com.claro.model.dto.IndicadorDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author andres
 */
public class UtilListas {
    
    public static List<PadreDTO> ordenarLista(List<PadreDTO> lstDTO, String orderby) {
        if (orderby != null && orderby.equals("nombre")) {
            EntityNameComparator  comparator = new EntityNameComparator();
            Collections.sort(lstDTO, comparator);
        } else if (orderby != null && orderby.equals("codigo")) {
            EntityIdComparator  comparator = new EntityIdComparator();
            Collections.sort(lstDTO, comparator);
        } else if (orderby != null && orderby.equals("fechacre")) {
            EntityFechaCreacionComparator  comparator = new EntityFechaCreacionComparator();
            Collections.sort(lstDTO, comparator);
        }       
        return lstDTO;
    }
    
    /**
     *
     * @param lstDTO
     * @param orderby
     * @return
     */
    public static List<IndicadorDTO> ordenarListaIndicadores(List<IndicadorDTO> lstDTO, String orderby) {
        if (orderby != null && orderby.equals("nombre")) {
            lstDTO.sort((IndicadorDTO h1, IndicadorDTO h2) -> h1.getNombreFormula().compareTo(h2.getNombreFormula()));
        } else if (orderby != null && orderby.equals("codigo")) {
            lstDTO.sort((IndicadorDTO h1, IndicadorDTO h2) -> h1.getId().compareTo(h2.getId()));
        } else if (orderby != null && orderby.equals("descripcion")) {
            lstDTO.sort((IndicadorDTO h1, IndicadorDTO h2) -> h1.getDescripcion().compareTo(h2.getDescripcion()));
        }        
        return lstDTO;
    }
    
}
