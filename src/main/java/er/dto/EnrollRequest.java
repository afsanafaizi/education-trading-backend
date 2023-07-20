package er.dto;

import lombok.Data;

@Data
public class EnrollRequest {
    private UserDTO userDTO;
    private CourseDTO courseDTO;
}