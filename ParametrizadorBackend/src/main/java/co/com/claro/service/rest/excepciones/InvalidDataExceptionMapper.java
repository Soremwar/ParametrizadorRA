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
public class InvalidDataExceptionMapper implements ExceptionMapper<DataNotFoundException>{

    @Override
    public Response toResponse(DataNotFoundException exception) {
        Mensaje mensaje = new Mensaje(404, "Datos Invalidos", exception.getCause().toString());
        return Response.status(Response.Status.NOT_FOUND).entity(mensaje).build();
    }
    
    
}
