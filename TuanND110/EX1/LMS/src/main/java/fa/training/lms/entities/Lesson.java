package fa.training.lms.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Khóa chính riêng của bảng Lesson để quản lý dễ dàng hơn

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @Column(name = "duration")
    private Integer duration; // Thời lượng (phút)

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType; // Video, Lý thuyết, Thực hành

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LessonStatus status; // Bản nháp, Đang mở, Đã khóa

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
            @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    @JsonIgnoreProperties("lessons")
    private Course course;
}
