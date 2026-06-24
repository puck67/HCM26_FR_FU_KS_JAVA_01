package com.lms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Author name is required")
    private String authorName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotEmpty(message = "Content is required")
    private String content;

    @NotNull(message = "Status is required")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Review() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Review{id=").append(id)
          .append(", authorName='").append(authorName).append('\'')
          .append(", email='").append(email).append('\'')
          .append(", rating=").append(rating)
          .append(", content='").append(content).append('\'')
          .append(", status=").append(status)
          .append('}');
        return sb.toString();
    }
}
