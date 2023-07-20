package er.resource;

import er.dto.LectureDTO;
import er.service.LectureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/lectures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LectureResource {

    @Inject
    LectureService lectureService;

    @POST
    public Response createLecture (LectureDTO lectureDTO) {
        LectureDTO createdLecture = lectureService.createLecture(lectureDTO);
        return Response.ok(createdLecture).build();
    }

    @GET
    public Response getAllLectures () {
        List<LectureDTO> allLectures = lectureService.getAllLectures();
        return Response.ok(allLectures).build();
    }

    @GET
    @Path("/{id}")
    public Response getLectureById (@PathParam("id") long id) {
        LectureDTO lecture = lectureService.getLectureById(id);
        if (lecture != null) {
            return Response.ok(lecture).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLecture (@PathParam("id") long id) {
        lectureService.deleteLecture(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateLecture (@PathParam("id") long id, LectureDTO updatedLectureDTO) {
        LectureDTO updatedLecture = lectureService.updateLecture(id, updatedLectureDTO);
        if (updatedLecture != null) {
            return Response.ok(updatedLecture).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}