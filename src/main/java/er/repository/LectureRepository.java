package er.repository;

import er.model.Lecture;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LectureRepository implements PanacheRepository<Lecture> {}