/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

import co.com.claro.service.rest.utils.MensajeError;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author andres
 */
@Provider
public class FormatoIncorrectoMapper implements ExceptionMapper<FormatoIncorrecto>{

    @Override
    public Response toResponse(FormatoIncorrecto exception) {
        MensajeError mensaje = new MensajeError(Response.Status.BAD_REQUEST.getStatusCode(), "Formato Incorrecto", exception.getCause().toString());
        return Response.status(Response.Status.BAD_REQUEST).entity(mensaje).build();
    }
    
    
}
