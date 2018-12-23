package co.com.claro.service.rest;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("/*")
public class OptionREST {

  @OPTIONS
  public Response findByAnyColumnOption() {
     return Response.ok().header(HttpHeaders.ALLOW, HttpMethod.POST).build();
  }
}
