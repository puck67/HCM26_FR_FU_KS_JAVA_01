package fa.training.lms.services;

import fa.training.lms.entities.Lesson;
import fa.training.lms.interfaces.LessonService;
import fa.training.lms.repositories.LessonRepository;
import fa.training.lms.services.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

import fa.training.lms.entities.ContentType;
import fa.training.lms.entities.LessonStatus;

@Service
public class LessonServiceImpl extends GenericServiceImpl<Lesson, Long> implements LessonService {
    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        super(lessonRepository);
        this.lessonRepository = lessonRepository;
    }

    @Override
    public java.util.List<Lesson> getLessonsByCourseId(fa.training.lms.entities.CourseId courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("CourseId must not be null");
        }
        return lessonRepository.findByCourseId(courseId);
    }

    @Override
    public Lesson save(Lesson entity) {
        if (entity.getLessonName() == null || entity.getLessonName().trim().isEmpty()) {
            throw new IllegalArgumentException("Lesson name must not be empty");
        }
        if (entity.getDuration() == null || entity.getDuration() <= 0) {
            throw new IllegalArgumentException("Lesson duration must be positive");
        }
        if (entity.getContentType() == null) {
            throw new IllegalArgumentException("Lesson content type must not be null");
        }
        if (entity.getStatus() == null) {
            throw new IllegalArgumentException("Lesson status must not be null");
        }
        return super.save(entity);
    }

    @Override
    public Lesson patch(Long id, java.util.Map<String, Object> updates) {
        Lesson existing = findById(id);
        if (existing == null) {
            throw new RuntimeException("Cannot patch. Lesson not found for ID: " + id);
        }

        if (updates.containsKey("lessonName")) {
            String lessonName = (String) updates.get("lessonName");
            if (lessonName == null || lessonName.trim().isEmpty()) {
                throw new IllegalArgumentException("Lesson name must not be empty");
            }
            existing.setLessonName(lessonName);
        }

        if (updates.containsKey("duration")) {
            Number duration = (Number) updates.get("duration");
            if (duration == null || duration.intValue() <= 0) {
                throw new IllegalArgumentException("Lesson duration must be positive");
            }
            existing.setDuration(duration.intValue());
        }

        if (updates.containsKey("contentType")) {
            Object contentTypeObj = updates.get("contentType");
            if (contentTypeObj == null) {
                throw new IllegalArgumentException("Lesson content type must not be null");
            }
            try {
                ContentType contentType = objectMapper.convertValue(contentTypeObj, ContentType.class);
                existing.setContentType(contentType);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid content type value: " + contentTypeObj);
            }
        }

        if (updates.containsKey("status")) {
            Object statusObj = updates.get("status");
            if (statusObj == null) {
                throw new IllegalArgumentException("Lesson status must not be null");
            }
            try {
                LessonStatus status = objectMapper.convertValue(statusObj, LessonStatus.class);
                existing.setStatus(status);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid status value: " + statusObj);
            }
        }

        return repository.update(existing);
    }
}
