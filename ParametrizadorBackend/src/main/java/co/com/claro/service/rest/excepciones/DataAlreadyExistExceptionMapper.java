/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

import co.com.claro.service.rest.response.WrapperResponseEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author andres
 */
@Provider
public class DataAlreadyExistExceptionMapper implements ExceptionMapper<DataAlreadyExistException>{

    @Override
    public Response toResponse(DataAlreadyExistException exception) {
        WrapperResponseEntity mensaje = new WrapperResponseEntity(404, "Ya existe esta informacion ", exception.getCause().toString());
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity(mensaje).build();
    }
    
    
}
