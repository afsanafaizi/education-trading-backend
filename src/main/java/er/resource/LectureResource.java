package er.resource;

import er.dto.FileUrlDTO;
import er.dto.LectureDTO;
import er.model.Course;
import er.repository.CourseRepository;
import er.service.LectureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Encoding;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Path("/lectures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LectureResource {
    private static final String UPLOADS_DIRECTORY = "upload";
    private static final String PHOTOS_DIRECTORY = "photos";
    private static final String VIDEOS_DIRECTORY = "videos";
    @Context
    UriInfo uriInfo;

    @Inject
    LectureService lectureService;


    @POST
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Upload file successfully"),
            @APIResponse(name = "500", responseCode = "500", description = "Internal service error")})
    @RequestBody(content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA,
            schema = @Schema(type = SchemaType.STRING, format = "binary"),
            encoding = @Encoding(name = "file", contentType = "application/octet-stream")))
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/upload")
    public Response uploadFile(@RestForm("file") List<FileUpload> files) throws IOException {
       List<String> urls = lectureService.uploadVideoToLecture(files,1l);
        return Response.accepted().entity(urls).build();
    }
    @POST
    public Response createLecture(LectureDTO lectureDTO) {
        LectureDTO createdLecture = lectureService.createLecture(lectureDTO);
        return Response.ok(createdLecture).build();
    }


    @GET
    @Path("/{id}")
    public Response getLectureById(@PathParam("id") long id) {
        LectureDTO lecture = lectureService.getLectureById(id);
        if (lecture != null) {
            return Response.ok(lecture).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLecture(@PathParam("id") long id) {
        lectureService.deleteLecture(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateLecture(@PathParam("id") long id, LectureDTO updatedLectureDTO) {
        LectureDTO updatedLecture = lectureService.updateLecture(id, updatedLectureDTO);
        if (updatedLecture != null) {
            return Response.ok(updatedLecture).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}