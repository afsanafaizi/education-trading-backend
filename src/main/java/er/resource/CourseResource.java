package er.resource;

import er.dto.CourseDTO;
import er.service.CourseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseResource {

    @Inject
    CourseService courseService;

    @POST
    public Response createCourse (CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return Response.ok(createdCourse).build();
    }

    @GET
    public Response getAllCourses () {
        List<CourseDTO> allCourses = courseService.getAllCourses();
        return Response.ok(allCourses).build();
    }

    @GET
    @Path("/{id}")
    public Response getCourseById (@PathParam("id") Long id) {
        CourseDTO course = courseService.getCourseById(id);
        if (course != null) {
            return Response.ok(course).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse (@PathParam("id") Long id) {
        courseService.deleteCourse(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCourse (@PathParam("id") Long id, CourseDTO updatedCourseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, updatedCourseDTO);
        if (updatedCourse != null) {
            return Response.ok(updatedCourse).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}