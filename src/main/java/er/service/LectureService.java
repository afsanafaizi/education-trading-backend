package er.service;

import er.dto.LectureDTO;
import er.model.Course;
import er.model.Lecture;
import er.model.Video;
import er.repository.CourseRepository;
import er.repository.LectureRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LectureService {

    @Inject
    LectureRepository lectureRepository;

    @Inject
    CourseRepository courseRepository;

    @Transactional
    public LectureDTO createLecture(LectureDTO lectureDTO) {
        Lecture lecture = new Lecture();
        lecture.setName(lectureDTO.getName());

        // Convert VideoDTOs to Video entities and associate with the Lecture.
        List<Video> videos = lectureDTO.getVideos().stream()
                                       .map(videoDTO -> {
                                           Video video = new Video();
                                           video.setName(videoDTO.getName());
                                           video.setVideoLength(videoDTO.getVideoLength());
                                           video.setLecture(lecture);
                                           return video;
                                       })
                                       .collect(Collectors.toList());

        lecture.setVideos(videos);

        Course course = courseRepository.findById(lectureDTO.getCourseId());
        if (course == null) {
            throw new NotFoundException("Course with id " + lectureDTO.getCourseId() + " not found");
        }

        lecture.setCourse(course);

        lectureRepository.persist(lecture);

        lectureDTO.setId(lecture.getId()); // Set the generated ID to the DTO for response.

        return lectureDTO;
    }

    // Add any other lecture-related business logic as needed.
}