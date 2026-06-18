package com.example.demo.controller;

import com.example.demo.controller.base.GenericController;
import com.example.demo.model.Course;
import com.example.demo.service.CourseService;
import com.example.demo.service.LookupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor/courses")
public class CourseController extends GenericController<Course, Long> {

    private final CourseService courseService;
    private final LookupService lookupService;

    public CourseController(CourseService courseService, LookupService lookupService) {
        super(courseService, "instructor", "course");
        this.courseService = courseService;
        this.lookupService = lookupService;
    }

    @Override
    protected Page<Course> getEntityPage(Pageable pageable) {
        return courseService.getAllCourses(pageable);
    }

    @Override
    protected Course createEmptyEntity() {
        return new Course();
    }

    @Override
    protected void setEntityId(Course entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected void beforeUpdate(Course entity, Long id) {
        Course existing = courseService.getCourseById(id);
        entity.setCreatedDate(existing.getCreatedDate());
    }

    @Override
    protected void addFormAttributes(Model model) {
        model.addAttribute("statuses", lookupService.getLookupsByType("CourseStatus"));
    }

    @GetMapping("/publish/{id}")
    public String publishCourse(@PathVariable("id") Long id) {
        courseService.updateCourseStatus(id, 2);
        return "redirect:/instructor/courses?published=true";
    }

    @GetMapping("/unpublish/{id}")
    public String unpublishCourse(@PathVariable("id") Long id) {
        courseService.updateCourseStatus(id, 1);
        return "redirect:/instructor/courses?unpublished=true";
    }

    @GetMapping("/archive/{id}")
    public String archiveCourse(@PathVariable("id") Long id) {
        courseService.updateCourseStatus(id, 3);
        return "redirect:/instructor/courses?archived=true";
    }
}
