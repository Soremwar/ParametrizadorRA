package co.com.claro.service.rest;


import co.com.claro.ejb.dao.PoliticaDAO;
import co.com.claro.model.entity.Politica;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ejb.EJB;
import javax.inject.Inject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andres
 */
@Path("politicas")
public class PoliticaRest {
    @EJB
    protected PoliticaDAO managerDAO;
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Politica> findAll() {
        List<Politica> lstPrueba = managerDAO.findAll();
        return lstPrueba;
    }        
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Politica entity) {
        managerDAO.create(entity);
        return Response.status(Response.Status.CREATED).entity(this).build();
    }
    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPolitica()
    {
        
        return Response.ok(lstPolitica).build();
        
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarPolitica(Politica politica) 
    {
        //lista.add(politica);
        return Response.status(Response.Status.CREATED).build();
        
    }
    */
}
