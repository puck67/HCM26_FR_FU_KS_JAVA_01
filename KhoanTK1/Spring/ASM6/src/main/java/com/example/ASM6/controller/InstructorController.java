package com.example.ASM6.controller;

import com.example.ASM6.model.Course;
import com.example.ASM6.model.Instructor;
import com.example.ASM6.model.Review;
import com.example.ASM6.service.CourseService;
import com.example.ASM6.service.InstructorService;
import com.example.ASM6.service.LookupService;
import com.example.ASM6.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
public class InstructorController {

    private final InstructorService instructorSrv;
    private final CourseService courseSrv;
    private final ReviewService reviewSrv;
    private final LookupService lookupSrv;

    public InstructorController(InstructorService instructorSrv, CourseService courseSrv,
                                ReviewService reviewSrv, LookupService lookupSrv) {
        this.instructorSrv = instructorSrv;
        this.courseSrv = courseSrv;
        this.reviewSrv = reviewSrv;
        this.lookupSrv = lookupSrv;
    }

    @ModelAttribute("lookupService")
    public LookupService getLookupService() {
        return lookupSrv;
    }

    @GetMapping("/instructor/login")
    public String showLoginForm(
            @RequestParam(name = "redirect", required = false) String redirectUrl,
            HttpSession sess,
            Model m) {
        if (sess.getAttribute("instructor") != null) {
            return "redirect:/instructor/dashboard";
        }
        m.addAttribute("redirect", redirectUrl);
        return "instructor/login";
    }

    @PostMapping("/instructor/login")
    public String login(
            @RequestParam("username") String user,
            @RequestParam("password") String pass,
            @RequestParam(name = "redirect", required = false) String redirectUrl,
            HttpSession sess,
            RedirectAttributes ra) {
        
        Optional<Instructor> auth = instructorSrv.login(user, pass);
        
        if (auth.isPresent()) {
            sess.setAttribute("instructor", auth.get());
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                return "redirect:" + redirectUrl;
            }
            return "redirect:/instructor/dashboard";
        } else {
            ra.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/instructor/login" + (redirectUrl != null ? "?redirect=" + redirectUrl : "");
        }
    }

    @GetMapping("/instructor/logout")
    public String logout(HttpSession sess) {
        sess.invalidate();
        return "redirect:/";
    }

    @GetMapping("/instructor/dashboard")
    public String dashboard(Model m) {
        m.addAttribute("totalCourses", courseSrv.countAllCourses());
        m.addAttribute("draftCourses", courseSrv.countCoursesByStatus(1));
        m.addAttribute("publishedCourses", courseSrv.countCoursesByStatus(2));
        m.addAttribute("archivedCourses", courseSrv.countCoursesByStatus(3));

        m.addAttribute("totalReviews", reviewSrv.countAllReviews());
        m.addAttribute("pendingReviewsCount", reviewSrv.countReviewsByStatus(1));
        m.addAttribute("approvedReviewsCount", reviewSrv.countReviewsByStatus(2));

        List<Review> pReviews = reviewSrv.getReviewsByStatus(1);
        m.addAttribute("pendingReviews", pReviews);

        return "instructor/dashboard";
    }

    @GetMapping("/instructor/courses")
    public String listCourses(Model m) {
        List<Course> list = courseSrv.getAllCourses();
        m.addAttribute("courses", list);
        return "instructor/courses";
    }

    @GetMapping("/instructor/courses/new")
    public String showCreateForm(Model m) {
        if (!m.containsAttribute("course")) {
            Course cObj = new Course();
            cObj.setStatus(1);
            m.addAttribute("course", cObj);
        }
        m.addAttribute("isEdit", false);
        return "instructor/course-form";
    }

    @GetMapping("/instructor/courses/edit/{id}")
    public String showEditForm(@PathVariable("id") Long cId, Model m) {
        Course cObj = courseSrv.getCourseById(cId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + cId));
        m.addAttribute("course", cObj);
        m.addAttribute("isEdit", true);
        return "instructor/course-form";
    }

    @PostMapping("/instructor/courses/save")
    public String saveCourse(
            @Valid @ModelAttribute("course") Course cObj,
            BindingResult result,
            Model m,
            RedirectAttributes ra) {

        if (result.hasErrors()) {
            m.addAttribute("isEdit", cObj.getId() != null);
            return "instructor/course-form";
        }

        courseSrv.saveCourse(cObj);
        ra.addFlashAttribute("successMessage", "Course saved successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/instructor/courses/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long cId, RedirectAttributes ra) {
        courseSrv.deleteCourse(cId);
        ra.addFlashAttribute("successMessage", "Course deleted successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/instructor/courses/status/{id}")
    public String changeStatus(
            @PathVariable("id") Long cId,
            @RequestParam("status") Integer statusVal,
            RedirectAttributes ra) {
        
        courseSrv.updateStatus(cId, statusVal);
        String txt = lookupSrv.getValue("COURSE_STATUS", String.valueOf(statusVal));
        ra.addFlashAttribute("successMessage", "Course status updated to " + txt + "!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/instructor/reviews")
    public String listReviews(Model m) {
        List<Review> pReviews = reviewSrv.getReviewsByStatus(1);
        List<Review> aReviews = reviewSrv.getReviewsByStatus(2);
        
        m.addAttribute("pendingReviews", pReviews);
        m.addAttribute("approvedReviews", aReviews);
        
        return "instructor/reviews";
    }

    @GetMapping("/instructor/reviews/approve/{id}")
    public String approveReview(@PathVariable("id") Long rId, RedirectAttributes ra) {
        reviewSrv.approveReview(rId);
        ra.addFlashAttribute("successMessage", "Review approved successfully!");
        return "redirect:/instructor/reviews";
    }

    @GetMapping("/instructor/reviews/delete/{id}")
    public String deleteReview(@PathVariable("id") Long rId, RedirectAttributes ra) {
        reviewSrv.deleteReview(rId);
        ra.addFlashAttribute("successMessage", "Review deleted successfully!");
        return "redirect:/instructor/reviews";
    }
}
