/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

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
 
        MensajeError mensaje = new MensajeError(500,  "Ocurrio un error interno en el servidor", exception.toString());
        if (exception.getCause() instanceof DataNotFoundException) {
            mensaje = new MensajeError(404, exception.getCause().getMessage(), Response.Status.NOT_FOUND.toString());
            return Response.status(Response.Status.NOT_FOUND).entity(mensaje).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(mensaje).build();
    }
    
}
