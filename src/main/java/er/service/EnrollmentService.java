package er.service;

import er.dto.CourseDTO;
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


}