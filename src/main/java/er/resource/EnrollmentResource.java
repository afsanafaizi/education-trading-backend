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
    @Path("/enroll")
    public Response enrollUserInCourse (EnrollRequest enrollRequest) {
        enrollmentService.enrollUserInCourse(enrollRequest.getUserDTO(), enrollRequest.getCourseDTO());
        return Response.ok().build();
    }

    @POST
    @Path("/un-enroll")
    public Response unEnrollUserFromCourse (EnrollRequest enrollRequest) {
        enrollmentService.unEnrollUserFromCourse(enrollRequest.getUserDTO(), enrollRequest.getCourseDTO());
        return Response.ok().build();
    }

    @GET
    @Path("/enrolled-users")
    public Response getAllEnrolledUsersInCourse (CourseDTO courseDTO) {
        List<UserDTO> enrolledUsers = enrollmentService.getAllEnrolledUsersInCourse(courseDTO);
        return Response.ok(enrolledUsers).build();
    }
}