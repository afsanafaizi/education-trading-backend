package er.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "video")
@Data
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private int videoLength;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

}