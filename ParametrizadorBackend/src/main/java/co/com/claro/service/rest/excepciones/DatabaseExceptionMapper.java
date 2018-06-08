/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author andresbedoya
 */
@Provider
public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException>{
    @Override
    public Response toResponse(DatabaseException exception) {
        MensajeError mensaje = new MensajeError(501, "Error inconsistencia de datos", exception.getMessage() +"... " + exception.getCause() +"... " + exception.getLocalizedMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(mensaje).build();
    }
}