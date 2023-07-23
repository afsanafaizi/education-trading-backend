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
    public Enrollment findByUserAndCourse(long userId, long courseId) {
        return find("user.id = ?1 and course.id = ?2", userId, courseId).firstResult();
    }

    public List<UserDTO> findEnrolledUsersByCourse(long courseId) {
        List<Enrollment> enrollments = find("course.id = ?1", courseId).list();
        return enrollments.stream().map(enrollment -> convertToUserDTO(enrollment.getUser()))
                .collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }
}