package er.dto;

import lombok.Data;

@Data
public class EnrolledCourseDTO {
    private long enrollmentId;
    private long courseId;
    private long userId;
    private String name;
    private String title;
    private String photo;
    private int completionPercentage;
}
