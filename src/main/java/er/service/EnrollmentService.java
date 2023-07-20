package er.service;

import er.dto.CourseDTO;
import er.dto.EnrollmentDTO;
import er.dto.UserDTO;
import er.model.Enrollment;
import er.repository.EnrollmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class EnrollmentService {

    @Inject
    EnrollmentRepository enrollmentRepository;

    @Transactional
    public void enrollUserInCourse (UserDTO userDTO, CourseDTO courseDTO) {
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setUser(userDTO);
        enrollmentDTO.setCourse(courseDTO);
        enrollmentRepository.persist(convertToEnrollment(enrollmentDTO));
    }

    @Transactional
    public void unEnrollUserFromCourse (UserDTO userDTO, CourseDTO courseDTO) {
        Enrollment enrollment = enrollmentRepository.findByUserAndCourse(userDTO, courseDTO);
        if (enrollment != null) {
            enrollmentRepository.delete(enrollment);
        }
    }

    @Transactional
    public List<UserDTO> getAllEnrolledUsersInCourse (CourseDTO courseDTO) {
        List<UserDTO> enrolledUsers = enrollmentRepository.findEnrolledUsersByCourse(courseDTO);
        return enrolledUsers;
    }

    // Add any other enrollment-related business logic as needed.

    private Enrollment convertToEnrollment (EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentDTO.getId());
        // Convert userDTO and courseDTO to entities if needed
        return enrollment;
    }

}