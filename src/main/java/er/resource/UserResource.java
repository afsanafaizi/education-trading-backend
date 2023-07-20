package er.resource;

import er.dto.UserDTO;
import er.service.UserService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Transactional
    public Response addUser (UserDTO user) {
        UserDTO savedUser = userService.createUser(user);
        return Response.ok(savedUser).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser (@PathParam("id") Long id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    public Response getAll () {
        List<UserDTO> user = userService.getAllUsers();
        return Response.ok(user).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser (long id) {
        userService.delete(id);
        return Response.ok("user deleted").build();
    }


    // Add other user-related endpoints as needed.
}