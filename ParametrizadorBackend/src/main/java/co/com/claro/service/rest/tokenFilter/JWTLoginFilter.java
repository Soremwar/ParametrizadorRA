package co.com.claro.service.rest.tokenFilter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;


@Provider
@JWTLogin
@Priority(Priorities.AUTHENTICATION)
public class JWTLoginFilter implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		// Recupera la cabecera HTTP Authorization de la petición
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
       
        try {
        	// Extrae el token de la cabecera
            String token = authorizationHeader.substring("Bearer".length()).trim();
            
            // Valida el token utilizando la cadena secreta
            Jws<Claims> claims = Jwts.parser().setSigningKey("fGiAxZZQAZ6BJ5WVblvcvWDRZ1MWFvTj").parseClaimsJws(token);
                              
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
		
	}

}
