package er.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private long id;
    private String name;
    private int videoLength;
    private long lectureId; // ID of the associated Lecture

    // Constructors, getters, setters, etc.

    public VideoDTO() {
    }

    public VideoDTO(long id, String name, int videoLength, long lectureId) {
        this.id = id;
        this.name = name;
        this.videoLength = videoLength;
        this.lectureId = lectureId;
    }

}