/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.cors;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Clase que maneja el cors de la aplicacion.
 * @author andres
 */
@Provider
public class NewCrossOriginResourceSharingFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
    	response.getHeaders().putSingle("Content-Type","application/json");
    	response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "access-control-allow-headers,access-control-allow-methods,access-control-allow-origin,authorization,content-type");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,HEAD,OPTIONS");
        response.getHeaders().putSingle("Access-Control-Expose-Headers", "access-control-allow-headers,access-control-allow-methods,access-control-allow-origin,authorization,content-type");
    }
    
}
