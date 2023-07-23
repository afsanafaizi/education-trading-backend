package er.util;

import er.dto.CourseDTO;
import jakarta.ws.rs.FormParam;
import lombok.Data;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Data
public class CourseForm {
    @RestForm("name")
    private String name;
    @RestForm("title")
    private String title;
    @RestForm("file")
    private FileUpload file;
}
