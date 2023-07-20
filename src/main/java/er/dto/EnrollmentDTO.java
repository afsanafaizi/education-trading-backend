package er.dto;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private Long id;
    private UserDTO user;
    private CourseDTO course;
    // Constructors, getters, setters, etc.
}