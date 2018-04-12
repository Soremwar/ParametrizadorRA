/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao.utils.comparators;

import co.com.claro.model.dto.ConciliacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import co.com.claro.model.dto.PoliticaDTO;
import co.com.claro.model.entity.Politica;
import java.util.Comparator;

/**
 *
 * @author andres
 */
public class EntityNameComparator implements Comparator<PadreDTO>{

    @Override
    public int compare(PadreDTO o1, PadreDTO o2) {
        return o1.getNombre() != null ? o1.getNombre().compareTo(o2.getNombre()) : 0;
    }
}
