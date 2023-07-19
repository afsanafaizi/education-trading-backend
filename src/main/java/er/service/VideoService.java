package er.service;

import er.dto.VideoDTO;
import er.model.Lecture;
import er.model.Video;
import er.repository.LectureRepository;
import er.repository.VideoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class VideoService {

    @Inject
    VideoRepository videoRepository;

    @Inject
    LectureRepository lectureRepository;

    @Transactional
    public VideoDTO uploadVideo(VideoDTO videoDTO, long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId);
        if (lecture == null) {
            throw new NotFoundException("Lecture with id " + lectureId + " not found");
        }

        Video video = new Video();
        video.setName(videoDTO.getName());
        video.setVideoLength(videoDTO.getVideoLength());
        video.setLecture(lecture);

        videoRepository.persist(video);

        videoDTO.setId(video.getId()); // Set the generated ID to the DTO for response.

        return videoDTO;
    }

    // Add any other video-related business logic as needed.
}