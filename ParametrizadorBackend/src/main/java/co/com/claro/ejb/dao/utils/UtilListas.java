/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao.utils;

import co.com.claro.ejb.dao.utils.comparators.EntityFechaCreacionComparator;
import co.com.claro.ejb.dao.utils.comparators.EntityIdComparator;
import co.com.claro.ejb.dao.utils.comparators.EntityNameComparator;
import co.com.claro.model.dto.EjecucionProcesoDTO;
import co.com.claro.model.dto.EscenarioDTO;
import co.com.claro.model.dto.IndicadorDTO;
import co.com.claro.model.dto.ParametroDTO;
import co.com.claro.model.dto.WsTransformacionDTO;
import co.com.claro.model.dto.parent.PadreDTO;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author andres
 */
public class UtilListas {
    
    /**
     * 
     * @param lstDTO
     * @param orderby
     * @return 
     */
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
    
    /**
     *
     * @param lstDTO
     * @param orderby
     * @return
     */
    public static List<ParametroDTO> ordenarListaParametros(List<ParametroDTO> lstDTO, String orderby) {
        if (orderby != null && orderby.equals("parametro")) {
            lstDTO.sort((ParametroDTO h1, ParametroDTO h2) -> h1.getParametro().compareTo(h2.getParametro()));
        } else if (orderby != null && orderby.equals("descripcion")) {
            lstDTO.sort((ParametroDTO h1, ParametroDTO h2) -> h1.getDescripcion().compareTo(h2.getDescripcion()));
        }        
        return lstDTO;
    }
    
    
    /**
     *
     * @param lstDTO
     * @param orderby
     * @return
     */
    public static List<WsTransformacionDTO> ordenarListaTransormacion(List<WsTransformacionDTO> lstDTO, String orderby) {
        if (orderby != null && orderby.equals("nombre")) {
            lstDTO.sort((WsTransformacionDTO h1, WsTransformacionDTO h2) -> h1.getNombreWs().compareTo(h2.getNombreWs()));
        } else if (orderby != null && orderby.equals("paquete")) {
            lstDTO.sort((WsTransformacionDTO h1, WsTransformacionDTO h2) -> h1.getPaqueteWs().compareTo(h2.getPaqueteWs()));
        }        
        return lstDTO;
    }

    
     /**
     *
     * @param lstDTO
     * @param orderby
     * @return
     */
    public static List<EscenarioDTO> ordenarListaEscenario(List<EscenarioDTO> lstDTO, String orderby) {
        if (orderby != null && orderby.equals("nombre")) {
            lstDTO.sort((EscenarioDTO h1, EscenarioDTO h2) -> h1.getNombre().compareTo(h2.getNombre()));
        } else if (orderby != null && orderby.equals("impacto")) {
            lstDTO.sort((EscenarioDTO h1, EscenarioDTO h2) -> h1.getImpacto().compareTo(h2.getImpacto()));
        }        
        return lstDTO;
    }
    
     /**
     *
     * @param lstDTO
     * @param orderby
     * @return
     */
    public static List<EjecucionProcesoDTO> ordenarListaEjecucion(List<EjecucionProcesoDTO> lstDTO, String orderby) {
        if (orderby != null && orderby.equals("nombre")) {
            lstDTO.sort((EjecucionProcesoDTO h1, EjecucionProcesoDTO h2) -> h1.getNombre().compareTo(h2.getNombre()));
        } else if (orderby != null && orderby.equals("componenteejecutado")) {
            lstDTO.sort((EjecucionProcesoDTO h1, EjecucionProcesoDTO h2) -> h1.getComponenteEjecutado().compareTo(h2.getComponenteEjecutado()));
        }        
        return lstDTO;
    }
      
}
