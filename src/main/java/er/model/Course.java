package er.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String title;
    private String photo;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lecture> lectures = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();
}