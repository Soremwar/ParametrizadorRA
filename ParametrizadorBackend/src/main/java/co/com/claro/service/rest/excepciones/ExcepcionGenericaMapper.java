/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author andres
 */
@Provider
public class ExcepcionGenericaMapper implements ExceptionMapper<Throwable>{

    @Override
    public Response toResponse(Throwable exception) {
 
        Mensaje mensaje = new Mensaje(500, exception.toString(), "Error" + exception.getCause().getMessage() );
        if (exception.getCause() instanceof DataNotFoundException) {
            mensaje = new Mensaje(404, Response.Status.NOT_FOUND.getReasonPhrase(), exception.getCause().getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(mensaje).build();
        } else if (exception.getCause() instanceof PersistenceException || exception.getCause() instanceof DatabaseException) {
            mensaje = new Mensaje(409, Response.Status.CONFLICT.getReasonPhrase(), exception.getCause().getMessage());
            return Response.status(Response.Status.CONFLICT).entity(mensaje).build();
            
        } 
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(mensaje).build();
    }
    
}
