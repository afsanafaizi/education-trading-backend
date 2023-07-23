package er.service;

import er.dto.LectureDTO;
import er.dto.VideoDTO;
import er.model.Course;
import er.model.Lecture;
import er.model.Video;
import er.repository.CourseRepository;
import er.repository.LectureRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class LectureService {
    private static final String UPLOADS_DIRECTORY = "/uploads";
    private static final String PHOTOS_DIRECTORY = "photos";
    private static final String VIDEOS_DIRECTORY = "videos";

    @ConfigProperty(name = "quarkus.http.body.uploads-directory")
    String directory;
    @Context
    UriInfo uriInfo;

    @Inject
    LectureRepository lectureRepository;

    @Inject
    CourseRepository courseRepository;

    @Transactional
    public LectureDTO createLecture(LectureDTO lectureDTO) {
        Lecture lecture = new Lecture();
        lecture.setName(lectureDTO.getName());

        // Convert VideoDTOs to Video entities and associate with the Lecture.
//        List<Video> videos = lectureDTO.getVideos().stream()
//                                       .map(videoDTO -> {
//                                           Video video = new Video();
//                                           video.setName(videoDTO.getName());
//                                           video.setVideoLength(videoDTO.getVideoLength());
//                                           video.setLecture(lecture);
//                                           return video;
//                                       })
//                                       .collect(Collectors.toList());

//        lecture.setVideos(videos);

        Course course = courseRepository.findById(lectureDTO.getCourseId());
        if (course == null) {
            throw new NotFoundException("Course with id " + lectureDTO.getCourseId() + " not found");
        }

        lecture.setCourse(course);

        lectureRepository.persist(lecture);

        lectureDTO.setId(lecture.getId()); // Set the generated ID to the DTO for response.

        return lectureDTO;
    }

    public List<String> uploadVideoToLecture(List<FileUpload> files, long lectureId) throws IOException {
        List<String> imageMimeTypes = Arrays.asList("image/jpg", "image/jpeg", "image/gif", "image/png");
        List<String> videoMimeTypes = Arrays.asList("video/mp4", "video/mpeg", "video/quicktime");
        List<String> urls = new ArrayList<>();
        if (files.size() > 0) {
            for (FileUpload file : files) {
                Map<String, String> newUrl = new HashMap<>();
                String fileType = file.contentType();
                String fileName = UUID.randomUUID() + "-" + file.fileName();
                File folder;

                if (imageMimeTypes.contains(fileType)) {
                    folder = createDirectoryIfNotExists(directory, PHOTOS_DIRECTORY);

                } else if (videoMimeTypes.contains(fileType)) {
                    folder = createDirectoryIfNotExists(directory, VIDEOS_DIRECTORY);

                } else {
                    throw new IOException("File not supported");
                }

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                Files.copy(file.filePath(), Paths.get(folder + File.separator + fileName));
                String baseURL = uriInfo.getBaseUri().getScheme() + "://" + uriInfo.getBaseUri().getAuthority() + "/";
                String fullPath = baseURL + "uploads/" + (imageMimeTypes.contains(fileType) ? "photos/" : "videos/") + fileName;
                urls.add(fullPath);
            }

        }
        return urls;

    }
    public LectureDTO getLectureById(long id) {
        Lecture lecture = lectureRepository.findById(id);
        if (lecture != null) {
            return convertToLectureDTO(lecture);
        }
        return null;
    }

    @Transactional
    public void deleteLecture(long id) {
        Lecture lecture = lectureRepository.findById(id);
        if (lecture != null) {
            lectureRepository.delete(lecture);
        }
    }

    @Transactional
    public LectureDTO updateLecture(long id, LectureDTO updatedLectureDTO) {
        Lecture lecture = lectureRepository.findById(id);
        if (lecture != null) {
            lecture.setName(updatedLectureDTO.getName());

            // Update or add new videos based on the DTO.
//            List<Video> updatedVideos = updatedLectureDTO.getVideos().stream().map(videoDTO -> {
//                Video video;
//                if (videoDTO.getId() > 0) {
//                    video = lecture.getVideos().stream()
//                                   .filter(existingVideo -> existingVideo.getId() == videoDTO.getId()).findFirst()
//                                   .orElse(null);
//                } else {
//                    video = new Video();
//                    video.setLecture(lecture);
//                }
//                video.setName(videoDTO.getName());
//                video.setVideoLength(videoDTO.getVideoLength());
//                return video;
//            }).collect(Collectors.toList());
//
//            lecture.setVideos(updatedVideos);

            Course course = courseRepository.findById(updatedLectureDTO.getCourseId());
            if (course == null) {
                throw new NotFoundException("Course with id " + updatedLectureDTO.getCourseId() + " not found");
            }

            lecture.setCourse(course);

            lectureRepository.persist(lecture);

            return convertToLectureDTO(lecture);
        }
        return null;
    }

    private LectureDTO convertToLectureDTO(Lecture lecture) {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(lecture.getId());
        lectureDTO.setName(lecture.getName());
        lectureDTO.setCourseId(lecture.getCourse().getId());

        // Convert Video entities to VideoDTOs
        List<VideoDTO> videoDTOs = lecture.getVideos().stream().map(this::convertToVideoDTO)
                .collect(Collectors.toList());

//        lectureDTO.setVideos(videoDTOs);

        return lectureDTO;
    }

    private VideoDTO convertToVideoDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(video.getId());
        videoDTO.setName(video.getName());
        videoDTO.setVideoLength(video.getVideoLength());
        // Since LectureDTO does not have a direct reference to VideoDTO, we skip setting the LectureDTO for the
        // videoDTO here.
        return videoDTO;
    }
    private File createDirectoryIfNotExists(String parentDirName, String childDirName) {
        File parentDir = new File(parentDirName+UPLOADS_DIRECTORY);
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