package er.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public Response register () {
        return Response.status(201).entity("created").build();
    }

    @GET
    public Response get () {
        return Response.status(200).entity("List").build();
    }

    @GET
    @Path("{id}")
    public Response getById (@PathParam("id") long id) {
        return Response.status(200).entity("Single").build();
    }

    @PUT
    @Path("{id}")
    public Response update (@PathParam("id") long id) {
        return Response.status(204).entity(" updated").build();
    }

    @DELETE
    @Path("{id}")
    public Response delete (@PathParam("id") long id) {
        return Response.status(204).entity(" Deleted").build();
    }

}