package er.resource;

import er.dto.CourseDTO;
import er.dto.EnrollRequest;
import er.dto.UserDTO;
import er.service.EnrollmentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/enrollments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnrollmentResource {

    @Inject
    EnrollmentService enrollmentService;

    @POST
    @Path("/enroll/{courseId}/{userId}")
    public Response enrollUserInCourse(long courseId, long userId) {
        enrollmentService.enrollUserInCourse(courseId, userId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/un-enroll/{courseId}/{userId}")
    public Response unEnrollUserFromCourse(long userId, long courseId) {
        enrollmentService.unEnrollUserFromCourse(userId, courseId);
        return Response.ok().build();
    }

    @GET
    @Path("/enrolled-users/{courseId}")
    public Response getAllEnrolledUsersInCourse(long courseId) {
        List<UserDTO> enrolledUsers = enrollmentService.getAllEnrolledUsersInCourse(courseId);
        return Response.ok(enrolledUsers).build();
    }
}