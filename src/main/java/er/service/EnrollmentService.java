package er.service;

import er.dto.CourseDTO;
import er.dto.EnrolledCourseDTO;
import er.dto.EnrollmentDTO;
import er.dto.UserDTO;
import er.model.Course;
import er.model.Enrollment;
import er.model.User;
import er.repository.CourseRepository;
import er.repository.EnrollmentRepository;
import er.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EnrollmentService {

    @Inject
    EnrollmentRepository enrollmentRepository;

    @Inject
    CourseRepository courseRepository;

    @Inject
    UserRepository userRepository;


    @Transactional
    public void enrollUserInCourse(long courseId, long userId) {

        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new NotFoundException("Course Not Available");
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Enrollment alreadyEnrolled = enrollmentRepository.find("user.id = ?1", userId).firstResult();
        if (alreadyEnrolled != null) {
            throw new WebApplicationException("User Already Enrolled", Response.Status.CONFLICT);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);

        enrollmentRepository.persist(enrollment);
    }

    @Transactional
    public void unEnrollUserFromCourse(long userId, long courseId) {
        enrollmentRepository.delete("user.id = ?1 and course.id = ?2", userId, courseId);
        // Find the enrollment using JPQL-style query
//        Enrollment enrollment = enrollmentRepository.getEntityManager().createQuery(
//                        "SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId", Enrollment.class)
//                .setParameter("userId", userId)
//                .setParameter("courseId", courseId)
//                .getSingleResult();
//
//        // Check if the enrollment exists
//        if (enrollment == null) {
//            throw new NotFoundException("Enrollment not found");
//        }
//
//        // Delete the enrollment
//        enrollmentRepository.delete(enrollment);
    }

    @Transactional
    public List<UserDTO> getAllEnrolledUsersInCourse(long courseId) {
        List<UserDTO> enrolledUsers = enrollmentRepository.findEnrolledUsersByCourse(courseId);
        return enrolledUsers;
    }
    @Transactional
    public void updateEnrollmentStatusAndPercentage(long enrollmentId, int completionPercentage, long userId) {
        Enrollment enrollment = enrollmentRepository.find("id = ?1 and user.id =?2",enrollmentId,userId).firstResult();
        if (enrollment == null) {
            throw new NotFoundException("Enrollment not found");
        }
        enrollment.setCompletionPercentage(completionPercentage);
        enrollmentRepository.getEntityManager().merge(enrollment);
    }

    public List<EnrolledCourseDTO> getAllEnrolledCoursesByUser(long userId) {
        return enrollmentRepository.find("user.id", userId).stream()
                .map(this::convertToEnrolledCourseDTO)
                .collect(Collectors.toList());
    }
    private EnrolledCourseDTO convertToEnrolledCourseDTO(Enrollment enrollment) {
        EnrolledCourseDTO enrolledCourseDTO = new EnrolledCourseDTO();
        enrolledCourseDTO.setEnrollmentId(enrollment.getId());
        enrolledCourseDTO.setCourseId(enrollment.getCourse().getId());
        enrolledCourseDTO.setUserId(enrollment.getUser().getId());
        enrolledCourseDTO.setName(enrollment.getCourse().getName());
        enrolledCourseDTO.setTitle(enrollment.getCourse().getTitle());
        enrolledCourseDTO.setPhoto(enrollment.getCourse().getPhoto());
        enrolledCourseDTO.setCompletionPercentage(enrollment.getCompletionPercentage());
        return enrolledCourseDTO;
    }

}