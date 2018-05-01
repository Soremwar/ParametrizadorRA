/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao.utils;

import co.com.claro.ejb.dao.utils.comparators.EntityFechaCreacionComparator;
import co.com.claro.ejb.dao.utils.comparators.EntityIdComparator;
import co.com.claro.ejb.dao.utils.comparators.EntityNameComparator;
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
    
}
