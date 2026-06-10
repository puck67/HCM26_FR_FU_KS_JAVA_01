package com.example.Ex1.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lesson")
@Data

@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
            @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    private Course course;

    @Column(name = "lesson_name")
    @NotBlank(message = "Tên bài học không được để trống")
    private String lessonName;

    @Column(name = "duration")
    @NotNull(message = "Thời lượng không được để trống")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    private Integer duration;

    @Column(name = "content_type")
    @NotBlank(message = "Loại nội dung không được để trống")
    private String contentType;

    @Column(name = "status")
    @NotBlank(message = "Trạng thái không được để trống")
    private String status;

    public Lesson() {
    }

}
