package er.repository;

import er.dto.CourseDTO;
import er.dto.UserDTO;
import er.model.Course;
import er.model.Enrollment;
import er.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EnrollmentRepository implements PanacheRepository<Enrollment> {
    public Enrollment findByUserAndCourse (UserDTO user, CourseDTO course) {
        // Convert userDTO and courseDTO to entities if needed
        User userEntity = convertToUser(user);
        Course courseEntity = convertToCourse(course);
        return find("user = ?1 and course = ?2", userEntity, courseEntity).firstResult();
    }

    public List<UserDTO> findEnrolledUsersByCourse (CourseDTO course) {
        Course courseEntity = convertToCourse(course);
        List<Enrollment> enrollments = find("course = ?1", courseEntity).list();
        return enrollments.stream().map(enrollment -> convertToUserDTO(enrollment.getUser()))
                          .collect(Collectors.toList());
    }

    private User convertToUser (UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        // Set other user properties if needed
        return user;
    }

    private Course convertToCourse (CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        // Set other course properties if needed
        return course;
    }

    private UserDTO convertToUserDTO (User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        // Set other userDTO properties if needed
        return userDTO;
    }
}