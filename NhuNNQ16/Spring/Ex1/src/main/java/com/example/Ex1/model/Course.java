package com.example.Ex1.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

//
@Entity
@Table(name = "course")
@IdClass(CourseId.class)
@Data

@AllArgsConstructor
public class Course {

    @Id
    @Column(name = "course_code")
    @NotBlank(message = "Mã khóa học không được để trống")
    private String courseCode;

    @Id
    @Column(name = "start_date")
    @NotNull(message = "Ngày khai giảng không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "course_name")
    @NotBlank(message = "Tên khóa học không được để trống")
    private String courseName;

    @Column(name = "category")
    @NotBlank(message = "Danh mục học tập không được để trống")
    private String category;

    @Column(name = "instructor")
    @NotBlank(message = "Giảng viên không được để trống")
    private String instructor;

    public Course() {
    }




}

