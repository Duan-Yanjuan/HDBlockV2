/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import entity.HDBlockUserEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author David
 */
@Path("HDBlockUser")
public class HDBlockUserResource {

    HDBlockUserEntityControllerLocal hDBlockUserEntityController = lookupHDBlockUserEntityControllerLocal();

  
    
    @Context
    private UriInfo context;

    
    
    /**
     * Creates a new instance of HDBlockUserResource
     */
    public HDBlockUserResource() {
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login/{email}/{password}")
    public Response registerAccount(@PathParam("email") String email, @PathParam("password") String password )
    {
        try
        {
           
            System.out.println("********** Login Attempt" + email + " password = " +  password);
            HDBlockUserEntity newUser = new HDBlockUserEntity();
           // HDBlockUserEntity user = hDBlockUserEntityController.registerAccount(newUser, password);
           
           /* if(user == null)
                return Response.status(Response.Status.OK).entity(new RetrieveCustomerAccountRsp(false)).build();
 
            
            return Response.status(Response.Status.OK).entity(new RetrieveCustomerAccountRsp(true)).build(); */
               return Response.status(Response.Status.OK).build();
 
        }
        catch(Exception ex)
        {
            System.out.println("Exception err");
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of HDBlockUserResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }


    private HDBlockUserEntityControllerLocal lookupHDBlockUserEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (HDBlockUserEntityControllerLocal) c.lookup("java:global/HDBlockV1/HDBlockV1-ejb/HDBlockUserEntityController!ejb.session.stateless.HDBlockUserEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
