package co.com.claro.service.rest.tokenFilter;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.persistence.Transient;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.MacProvider;
import co.com.claro.service.rest.tokenFilter.JWTTokenNeeded;


@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter{
	
	public static final Key KEY = MacProvider.generateKey();
	public static final String KEYPAR = "Sdudphwul}dgruFodur534;bQlwror";   
	
	@Transient
   	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
 
        // Recupera la cabecera HTTP Authorization de la petición
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
       
        try {
        	// Extrae el token de la cabecera
            String token = authorizationHeader.substring("Bearer".length()).trim();
            
            // Valida el token utilizando la cadena secreta
            Jws<Claims> claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
                              
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

}
