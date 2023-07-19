package er.resource;

import er.service.FormData;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @ConfigProperty(name = "upload.folder.path.cards")
    String directory;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile (FormData data) throws IOException {
        File folder = new File(directory);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        List<String> mimetype = Arrays.asList("image/jpg", "image/jpeg", "image/gif", "image/png");
        if (!mimetype.contains(data.getFile().contentType())) {
            throw new IOException("File not supported");
        }
        if (data.getFile().size() > 1024 * 1024 * 4) {
            throw new IOException("Larger than 4mb file not allowed");
        }
        String fileName = UUID.randomUUID() + "-" + data.getFile().fileName();
        Files.copy(data.getFile().filePath(), Paths.get(folder + File.separator + fileName));
        return Response.accepted().entity(folder.toString()).build();
    }

}