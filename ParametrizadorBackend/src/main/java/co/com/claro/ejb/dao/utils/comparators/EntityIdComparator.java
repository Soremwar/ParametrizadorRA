/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao.utils.comparators;

import co.com.claro.model.dto.parent.PadreDTO;
import java.util.Comparator;

/**
 *
 * @author andres
 */
public class EntityIdComparator implements Comparator<PadreDTO>{

    @Override
    public int compare(PadreDTO o1, PadreDTO o2) {
        return o1.getId() != null ? o1.getId().compareTo(o2.getId()) : 0;
    }

}
