/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

import co.com.claro.service.rest.response.ResponseCode;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import javax.persistence.PersistenceException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.JsonProcessingException;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author andres
 */
@Provider
@Produces({MediaType.APPLICATION_JSON})
public class ExcepcionGenericaMapper implements ExceptionMapper<Throwable>{

    @Override
    public Response toResponse(Throwable e) {
        WrapperResponseEntity response = null;
        
        if (e instanceof NotFoundException || e.getCause() instanceof DataNotFoundException) {
            response = new WrapperResponseEntity(ResponseCode.NOT_FOUND, e.getCause() != null ? e.getCause().getMessage() : e.toString());
	} else if (e instanceof NotAllowedException) {
            response = new WrapperResponseEntity(ResponseCode.FORBIDDEN, e.getCause() != null ? e.getCause().getMessage() : e.toString());
	} else if (e instanceof JsonProcessingException) {
            response = new WrapperResponseEntity(ResponseCode.ERROR_JSON, e.getCause() != null ? e.getCause().getMessage() : e.toString());
	} else if (e instanceof NotSupportedException) {
            response = new WrapperResponseEntity(ResponseCode.UNSUPPORTED_MEDIA_TYPE, e.getCause() != null ? e.getCause().getMessage() : e.toString());
	} else if (e instanceof PersistenceException || e instanceof DatabaseException) {
           response = new WrapperResponseEntity(ResponseCode.CONFLICT, e.getCause() != null ? e.getCause().getMessage() : e.toString());            
        }
        if(response == null) {
            response = new WrapperResponseEntity(ResponseCode.INTERNAL_SERVER_ERROR, e.getCause() != null ? e.getCause().getMessage() : e.toString());
	}
        /*if (exception.getCause() instanceof DataNotFoundException) {
            mensaje = new WrapperResponseEntity(404, Response.Status.NOT_FOUND.getReasonPhrase(), exception.getCause().getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(mensaje).build();
        } else if (exception.getCause() instanceof NotAllowedException) {
            mensaje = new WrapperResponseEntity(404, Response.Status.FORBIDDEN.getReasonPhrase(), exception.getCause().getMessage());
            return Response.status(Response.Status.FORBIDDEN).entity(mensaje).build();
        } else if (exception.getCause() instanceof NotSupportedException) {
            mensaje = new WrapperResponseEntity(404, Response.Status.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), exception.getCause().getMessage());
            return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE).entity(mensaje).build();        
        } else if (exception.getCause() instanceof PersistenceException || exception.getCause() instanceof DatabaseException) {
            mensaje = new WrapperResponseEntity(409, Response.Status.CONFLICT.getReasonPhrase(), exception.getCause().getMessage());
            return Response.status(Response.Status.CONFLICT).entity(mensaje).build();
            
        } */
        return Response.status(response.httpStatus()).type(MediaType.APPLICATION_JSON).entity(response).build();
    }
    
}
