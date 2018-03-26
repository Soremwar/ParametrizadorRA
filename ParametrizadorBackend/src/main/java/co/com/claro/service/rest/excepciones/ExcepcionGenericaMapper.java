/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

import co.com.claro.model.entity.MensajeError;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author andres
 */
@Provider
public class ExcepcionGenericaMapper implements ExceptionMapper<Throwable>{

    @Override
    public Response toResponse(Throwable exception) {
        MensajeError mensaje = null;
        if (exception != null) {
            mensaje = new MensajeError(500, exception.getMessage(), exception.getCause().toString());
        } else {
            mensaje = new MensajeError(500, "Excepcion Generica", "Ocurrio un error...");
        }
        return Response.status(Response.Status.NOT_FOUND).entity(mensaje).build();
    }
    
}
