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
        String mensaje = "";
        String descripcion = e.getCause() != null ? e.getCause().getMessage() : e.toString();
        int indexStart = descripcion.indexOf("CTRAINT_");
        if (indexStart > 0) {
            int indexEnd = descripcion.indexOf(" ", indexStart) - 1;
            mensaje = descripcion.substring(indexStart, indexEnd);
        }

        if (e instanceof NotFoundException || e.getCause() instanceof DataNotFoundException) {
            response = new WrapperResponseEntity(ResponseCode.NOT_FOUND, mensaje, descripcion);
	} else if (e instanceof NotAllowedException) {
            response = new WrapperResponseEntity(ResponseCode.FORBIDDEN, mensaje, descripcion);
	} else if (e instanceof JsonProcessingException) {
            response = new WrapperResponseEntity(ResponseCode.ERROR_JSON, mensaje, descripcion);
	} else if (e instanceof NotSupportedException) {
            response = new WrapperResponseEntity(ResponseCode.UNSUPPORTED_MEDIA_TYPE, mensaje, descripcion);
	} else if (e instanceof PersistenceException || e instanceof DatabaseException) {
           response = new WrapperResponseEntity(ResponseCode.CONFLICT, mensaje, descripcion);            
        }
        if(response == null) {
            response = new WrapperResponseEntity(ResponseCode.INTERNAL_SERVER_ERROR, mensaje, descripcion);
	}
        return Response.status(response.httpStatus()).type(MediaType.APPLICATION_JSON).entity(response).build();
    }
    
}
