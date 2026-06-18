package com.example.ASM4.main;

import com.example.ASM4.model.Course;
import com.example.ASM4.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Configuration
@ComponentScan("com.example.ASM4")
public class Test {

	@Autowired
	private CourseService courseService;

	public void run() {
		// 1. Tạo danh sách Course và lưu vào file
		List<Course> courses = Arrays.asList(
				new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot", 40),
				new Course("Java OOP", "Jane Smith", "Master Object-Oriented Programming in Java", 30),
				new Course("Hibernate & JPA", "Bob Nguyen", "Database ORM with Hibernate", 25)
		);

		courseService.saveCourses(courses);

		// 2. Đọc lại từ file và hiển thị
		System.out.println("\n--- Courses loaded from file ---");
		List<Course> loadedCourses = courseService.getCourses();
		loadedCourses.forEach(Course::displayInfo);
	}

	public static void main(String[] args) {
		ApplicationContext context =
				new AnnotationConfigApplicationContext(Test.class);

		Test test = context.getBean(Test.class);
		test.run();
	}
}