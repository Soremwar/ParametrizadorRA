/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.ejb.dao;

import co.com.claro.model.entity.Resultado;
import co.com.claro.model.entity.WsTransformacion;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author ronal
 */
@Remote
public interface IWsTransformacionDAO {

    public List<WsTransformacion> findByFechaAgendamiento(Date fechaAgendamiento, Date fechaAgendamientoHasta);

    public List<WsTransformacion> findRange(int[] range);

    public WsTransformacion find(Object id);

    public List<WsTransformacion> findByAnyColumn(String busqueda);

    public void create(WsTransformacion entity);

    public WsTransformacion edit(WsTransformacion entity);

    public int count();

    public void remove(WsTransformacion entity);

    public List<WsTransformacion> verificarSiExistePaqueteWs(String paqueteWs);

    public List<WsTransformacion> validPaqueteWs(String paqueteWs);
}
