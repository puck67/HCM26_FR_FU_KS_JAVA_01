package com.example.training.config;


import com.example.training.entity.Course;
import com.example.training.entity.Role;
import com.example.training.entity.User;

import com.example.training.repository.CourseRepository;
import com.example.training.repository.RoleRepository;
import com.example.training.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Set;



@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {



    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final PasswordEncoder encoder;



    @Override
    public void run(String... args) {



        // =========================
        // CREATE ROLES
        // =========================


        Role adminRole =
                createRole("ADMIN");


        Role trainerRole =
                createRole("TRAINER");


        Role studentRole =
                createRole("STUDENT");





        // =========================
        // CREATE USERS
        // =========================


        createUser(

                "admin",

                "admin123",

                "System Admin",

                "admin@gmail.com",

                adminRole

        );




        createUser(

                "trainer",

                "trainer123",

                "John Trainer",

                "trainer@gmail.com",

                trainerRole

        );




        createUser(

                "student",

                "student123",

                "Peter Student",

                "student@gmail.com",

                studentRole

        );






        // =========================
        // CREATE COURSES
        // =========================


        if(courseRepository.count() == 0){



            Course course1 = new Course();


            course1.setCourseName(
                    "Spring Boot REST API"
            );


            course1.setDuration(
                    "40 hours"
            );


            course1.setDescription(
                    "Spring Boot JWT Security"
            );




            Course course2 = new Course();


            course2.setCourseName(
                    "React Tailwind CSS"
            );


            course2.setDuration(
                    "30 hours"
            );


            course2.setDescription(
                    "Frontend React Dashboard"
            );





            Course course3 = new Course();


            course3.setCourseName(
                    "Database Design"
            );


            course3.setDuration(
                    "20 hours"
            );


            course3.setDescription(
                    "JPA Hibernate Database"
            );



            courseRepository.save(course1);

            courseRepository.save(course2);

            courseRepository.save(course3);



        }



    }







    private Role createRole(String name){



        return roleRepository

                .findByRoleName(name)

                .orElseGet(() -> {


                    Role role = new Role();


                    role.setRoleName(name);


                    return roleRepository.save(role);


                });


    }







    private void createUser(

            String username,

            String password,

            String fullName,

            String email,

            Role role

    ){



        if(userRepository
                .findByUsername(username)
                .isEmpty()){



            User user = new User();



            user.setUsername(username);



            user.setPassword(
                    encoder.encode(password)
            );



            user.setFullName(
                    fullName
            );



            user.setEmail(
                    email
            );



            user.setStatus(
                    "Active"
            );



            user.setRoles(
                    Set.of(role)
            );



            userRepository.save(user);



        }


    }


}