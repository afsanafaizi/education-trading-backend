package er.resource;

import er.dto.FileUrlDTO;
import er.service.FormData;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UploadResource {


//    @ConfigProperty(name = "upload.folder.path.photos")
//    String photosDirectory;
//
//    @ConfigProperty(name = "upload.folder.path.videos")
//    String videosDirectory;

    private static final String UPLOADS_DIRECTORY = "upload";
    private static final String PHOTOS_DIRECTORY = "photos";
    private static final String VIDEOS_DIRECTORY = "videos";
    @Context
    UriInfo uriInfo;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(FormData data) throws IOException {
        List<String> imageMimeTypes = Arrays.asList("image/jpg", "image/jpeg", "image/gif", "image/png");
        List<String> videoMimeTypes = Arrays.asList("video/mp4", "video/mpeg", "video/quicktime");

        String fileType = data.getFile().contentType();
        String fileName = UUID.randomUUID() + "-" + data.getFile().fileName();
        File folder;

        if (imageMimeTypes.contains(fileType)) {
//            if (data.getFile().size() > 1024 * 1024 * 2) {
//                throw new IOException("Larger than 2mb file not allowed");
//            }
//            folder = new File(photosDirectory);
            folder = createDirectoryIfNotExists(UPLOADS_DIRECTORY, PHOTOS_DIRECTORY);

        } else if (videoMimeTypes.contains(fileType)) {
//            folder = new File(videosDirectory);
            folder = createDirectoryIfNotExists(UPLOADS_DIRECTORY, VIDEOS_DIRECTORY);

        } else {
            // Handle other types of files, if needed.
            throw new IOException("File not supported");
        }

        if (!folder.exists()) {
            folder.mkdirs();
        }

        Files.copy(data.getFile().filePath(), Paths.get(folder + File.separator + fileName));
        // Return the full path to the file for saving it in the database
//        String baseURL = uriInfo.getBaseUri().toString();
        String baseURL = uriInfo.getBaseUri().getScheme() + "://" + uriInfo.getBaseUri().getAuthority() + "/";
        String fullPath = baseURL + "uploads/" + (imageMimeTypes.contains(fileType) ? "photos/" : "videos/") + fileName;

        FileUrlDTO url = new FileUrlDTO();
        url.setUrl(fullPath);
//        String fullPath = folder + File.separator + fileName;
        return Response.accepted().entity(url).build();
    }

    private File createDirectoryIfNotExists(String parentDirName, String childDirName) {
        File parentDir = new File(parentDirName);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        File childDir = new File(parentDir, childDirName);
        if (!childDir.exists()) {
            childDir.mkdirs();
        }

        return childDir;
    }

//    @POST
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response uploadFile (FormData data) throws IOException {
//        File folder = new File(directory);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        List<String> mimetype = Arrays.asList("image/jpg", "image/jpeg", "image/gif", "image/png");
//        if (!mimetype.contains(data.getFile().contentType())) {
//            throw new IOException("File not supported");
//        }
//        if (data.getFile().size() > 1024 * 1024 * 4) {
//            throw new IOException("Larger than 4mb file not allowed");
//        }
//        String fileName = UUID.randomUUID() + "-" + data.getFile().fileName();
//        Files.copy(data.getFile().filePath(), Paths.get(folder + File.separator + fileName));
//        return Response.accepted().entity(folder.toString()).build();
//    }

}