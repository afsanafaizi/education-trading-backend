package er.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class LectureDTO {
    private long id;
    private String name;
    private List<VideoDTO> videos = new ArrayList<>();
    private long courseId;

    // Constructors, getters, setters, etc.

    public LectureDTO() {
    }

    public LectureDTO(long id, String name, List<VideoDTO> videos, long courseId) {
        this.id = id;
        this.name = name;
        this.videos = videos;
        this.courseId = courseId;
    }

    // Getters and setters for other fields (if needed).

    // Constructors, getters, setters, etc.
}