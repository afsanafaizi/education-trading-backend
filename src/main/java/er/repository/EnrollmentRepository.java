package er.repository;

import er.model.Enrollment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnrollmentRepository implements PanacheRepository<Enrollment> {
    // Add any custom methods related to Enrollment entity (if needed).
}