package er.service;

import er.dto.CourseDTO;
import er.dto.LectureDTO;
import er.model.Course;
import er.model.Enrollment;
import er.model.Lecture;
import er.repository.CourseRepository;
import er.repository.EnrollmentRepository;
import er.repository.LectureRepository;
import er.util.CourseForm;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class CourseService {

    private static final String UPLOADS_DIRECTORY = "uploads";
    private static final String PHOTOS_DIRECTORY = "photos";
    @ConfigProperty(name = "quarkus.http.body.uploads-directory")
    String directory;
    @Context
    UriInfo uriInfo;
    @Inject
    CourseRepository courseRepository;

    @Inject
    LectureRepository lectureRepository;

    @Inject
    EnrollmentRepository enrollmentRepository;

    @Transactional
    public void createCourse(CourseForm courseForm) throws IOException {

        List<String> imageMimeTypes = Arrays.asList("image/jpg", "image/jpeg", "image/gif", "image/png");
        String fileType = courseForm.getFile().contentType();
        String fileName = UUID.randomUUID() + "-" + courseForm.getFile().fileName();
        File folder;

        if (imageMimeTypes.contains(fileType)) {
            folder = createDirectoryIfNotExists(directory, PHOTOS_DIRECTORY);

        } else {
            throw new IOException("File not supported");
        }

        if (!folder.exists()) {
            folder.mkdirs();
        }

        Files.copy(courseForm.getFile().filePath(), Paths.get(folder + File.separator + fileName));
        String baseURL = uriInfo.getBaseUri().getScheme() + "://" + uriInfo.getBaseUri().getAuthority() + "/";
        String fullPath = baseURL +UPLOADS_DIRECTORY +"/" + PHOTOS_DIRECTORY + fileName;

        Course course = new Course();
        course.setName(courseForm.getName());
        course.setTitle(courseForm.getTitle());
        course.setPhoto(fullPath);
        courseRepository.persist(course);
//        courseDTO.setId(course.getId());
//        return courseDTO;
    }

    @Transactional
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id);
        if (course != null) {
            return convertToCourseDTO(course);
        }
        return null;
    }

    public List<LectureDTO> getLecturesByCourseId(@PathParam("id") Long id) {
        PanacheQuery<Lecture> panacheQuery = lectureRepository.find("course.id = ?1", id);
        if (panacheQuery.list().isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        }
        return panacheQuery.list().stream().map(this::convertToLectureDTO).collect(Collectors.toList());

    }

    @Transactional
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.listAll();
        return courses.stream().map(this::convertToCourseDTO).collect(Collectors.toList());
    }

    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO updatedCourseDTO) {
        Course course = courseRepository.findById(id);
        if (course != null) {
            course.setName(updatedCourseDTO.getName());

            courseRepository.persist(course);

            return convertToCourseDTO(course);
        }
        return null;
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id);
        if (course != null) {
            //: The Enrollment Delete should be removed this is dangerous operation
            List<Enrollment> enrollments = enrollmentRepository.find("course.id", id).list();
            for (Enrollment enrollment : enrollments) {
                enrollmentRepository.delete(enrollment);
            }
            courseRepository.delete(course);
        }
    }

    private CourseDTO convertToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setPhoto(course.getPhoto());
        return courseDTO;
    }

    private LectureDTO convertToLectureDTO(Lecture lecture) {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(lecture.getId());
        lectureDTO.setName(lecture.getName());
        lectureDTO.setCourseId(lecture.getCourse().getId());

        // Convert Video entities to VideoDTOs
//        List<VideoDTO> videoDTOs = lecture.getVideos().stream().map(this::convertToVideoDTO)
//                .collect(Collectors.toList());

//        lectureDTO.setVideos(videoDTOs);

        return lectureDTO;
    }

    private File createDirectoryIfNotExists(String parentDirName, String childDirName) {
        File parentDir = new File(parentDirName+"/"+ UPLOADS_DIRECTORY);
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        File childDir = new File(parentDir, childDirName);
        if (!childDir.exists()) {
            childDir.mkdirs();
        }

        return childDir;
    }
}