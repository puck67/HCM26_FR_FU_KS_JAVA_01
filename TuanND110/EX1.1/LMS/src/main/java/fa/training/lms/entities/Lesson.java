package fa.training.lms.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    @ToString.Exclude
    private Course course;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Lesson lesson = (Lesson) o;
        return getId() != null && Objects.equals(getId(), lesson.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
