/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.excepciones;

import co.com.claro.service.rest.i18n.I18N;
import co.com.claro.service.rest.response.ResponseCode;
import co.com.claro.service.rest.response.WrapperResponseEntity;
import co.com.claro.service.rest.util.ResponseWrapper;

import javax.ejb.TransactionRolledbackLocalException;
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
//import org.eclipse.persistence.exceptions.DatabaseException;

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
        int indexStart = descripcion.indexOf("CT_");
        if (indexStart > 0) {
            int indexEnd = descripcion.indexOf(" ", indexStart) - 2;
            mensaje = descripcion.substring(indexStart, indexEnd);
        }
        
        String ctError = getCTError(descripcion.toUpperCase());
        System.out.println("ctError ==> " + descripcion.toUpperCase());
        if(ctError != null) {
        	ResponseWrapper wrapper = new ResponseWrapper(false, I18N.getMessage(ctError), null);
        	return Response.ok(wrapper,MediaType.APPLICATION_JSON).build();
        }
        else if (e instanceof NotFoundException || e.getCause() instanceof DataNotFoundException) {
            response = new WrapperResponseEntity(ResponseCode.NOT_FOUND, mensaje, descripcion);
            
		} else if (e instanceof DataAlreadyExistException) {
	            response = new WrapperResponseEntity(ResponseCode.CONFLICT, mensaje, descripcion);
	            
		} else if (e instanceof NotAllowedException) {
	            response = new WrapperResponseEntity(ResponseCode.FORBIDDEN, mensaje, descripcion);
	            
		} else if (e instanceof JsonProcessingException) {
	            response = new WrapperResponseEntity(ResponseCode.ERROR_JSON, mensaje, descripcion);
	            
		} else if (e instanceof NotSupportedException) {
	            response = new WrapperResponseEntity(ResponseCode.UNSUPPORTED_MEDIA_TYPE, mensaje, descripcion);
	            
		} else if (e.getCause() instanceof PersistenceException  || e.getCause() instanceof TransactionRolledbackLocalException) {
           response = new WrapperResponseEntity(ResponseCode.CONFLICT, mensaje, descripcion);     
           
        }
        if(response == null) {
            response = new WrapperResponseEntity(ResponseCode.INTERNAL_SERVER_ERROR, mensaje, descripcion);
        }
        return Response.status(response.httpStatus()).type(MediaType.APPLICATION_JSON).entity(response).build();
    }
    
    	
    public String getCTError(String descripcion) {
    	if(descripcion.contains("CT_PK_TBL_GAI_POLITICA")) {
    		return "CT_PK_TBL_GAI_POLITICA";
    	}else if(descripcion.contains("CT_TBL_GAI_CONCILIACION_FK_POLITICA")){
    		return "CT_TBL_GAI_CONCILIACION_FK_POLITICA";
    	}else if(descripcion.contains("CT_TBL_GAI_ESCENARIO_FK_CONCILIACION")){
    		return "CT_TBL_GAI_ESCENARIO_FK_CONCILIACION";
    	}else if(descripcion.contains("CT_UQ_TBL_GAI_CONCILIACION_FK_POLITICA")){
    		return "CT_UQ_TBL_GAI_CONCILIACION_FK_POLITICA";
    	}else if(descripcion.contains("CT_UQ_TBL_GAI_CONCILIACION_NOMBRE_CONCILIACION")){
    		return "CT_UQ_TBL_GAI_CONCILIACION_NOMBRE_CONCILIACION";
    	}else if(descripcion.contains("CT_TBL_GAI_CONCILIACION_FK_POLITICA")){
    		return "CT_TBL_GAI_CONCILIACION_FK_POLITICA";
    	}else if(descripcion.contains("CT_UQ_TBL_GAI_ESCENARIO_NOMBRE_ESCENARIO")){
    		return "CT_UQ_TBL_GAI_ESCENARIO_NOMBRE_ESCENARIO";
    	}else if(descripcion.contains("CT_PK_TBL_GAI_ESCENARIO")){
    		return "CT_PK_TBL_GAI_ESCENARIO";
    	}else if(descripcion.contains("CT_UQ_TBL_GAI_PARAMETROS")){
    		return "CT_UQ_TBL_GAI_PARAMETROS";
    	}else if(descripcion.contains("CT_PK_TBL_GAI_QUERIES_ESCENARIOS")){
    		return "CT_PK_TBL_GAI_QUERIES_ESCENARIOS";
    	}else if(descripcion.contains("CT_UQ_TBL_GAI_QUERIES_ESCENARIOS_NOMBRE_QUERY")){
    		return "CT_UQ_TBL_GAI_QUERIES_ESCENARIOS_NOMBRE_QUERY";
    	}else if(descripcion.contains("CT_TBL_GAI_QUERIES_ESCENARIOS_FK_ESCENARIO")){
    		return "CT_TBL_GAI_QUERIES_ESCENARIOS_FK_ESCENARIO";
    	}else {
    		return null;
    	}
    }
    
}
