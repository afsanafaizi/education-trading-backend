package er.service;

import er.model.Enrollment;
import er.repository.EnrollmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EnrollmentService {

    @Inject
    EnrollmentRepository enrollmentRepository;

    public Enrollment saveEnrollment(Enrollment enrollment) {
        enrollmentRepository.persist(enrollment);
        return enrollment;
    }

    // Add any other enrollment-related business logic as needed.
}