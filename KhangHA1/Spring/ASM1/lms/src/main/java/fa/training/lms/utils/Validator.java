package fa.training.lms.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import fa.training.lms.model.Course;

@Component
public class Validator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Course course = (Course) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty.course.title", "Title must not be empty");
        if (course.getTitle() != null && course.getTitle().length() < 5) {
            errors.rejectValue("title", "Size.course.title", "Title must have at least 5 characters");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "instructorName", "NotEmpty.course.instructorName", "Instructor name must not be empty");
        if (course.getInstructorName() != null && course.getInstructorName().length() < 2) {
            errors.rejectValue("instructorName", "Size.course.instructorName", "Instructor name must have at least 2 characters");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "instructorEmail", "NotEmpty.course.instructorEmail", "Instructor email must not be empty");
        if (course.getInstructorEmail() != null && !course.getInstructorEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.rejectValue("instructorEmail", "Email.course.instructorEmail", "Must be a valid email address");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty.course.description", "Description must not be empty");
        if (course.getDescription() != null && (course.getDescription().length() < 10 || course.getDescription().length() > 200)) {
            errors.rejectValue("description", "Size.course.description", "Description must be between 10 and 200 characters long");
        }

        if (course.getDurationHours() == null) {
            errors.rejectValue("durationHours", "NotNull.course.durationHours", "Duration must not be null");
        } else if (course.getDurationHours() < 1) {
            errors.rejectValue("durationHours", "Min.course.durationHours", "Duration must be at least 1 hour");
        }
    }
}
