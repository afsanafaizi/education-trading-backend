package er.service;

import er.dto.CourseDTO;
import er.model.Course;
import er.repository.CourseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CourseService {

    @Inject
    CourseRepository courseRepository;

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.getName());

        courseRepository.persist(course);

        courseDTO.setId(course.getId()); // Set the generated ID to the DTO for response.

        return courseDTO;
    }

    @Transactional
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id);
        if (course != null) {
            return convertToCourseDTO(course);
        }
        return null;
    }

    @Transactional
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.listAll();
        return courses.stream()
                      .map(this::convertToCourseDTO)
                      .collect(Collectors.toList());
    }

    private CourseDTO convertToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        return courseDTO;
    }

}