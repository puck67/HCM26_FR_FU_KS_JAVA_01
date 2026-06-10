package fa.training.models;

public class Course {
    int course_code;
    int start_date;
    String course_name;
    String category;
    String instructor;

    public Course() {
    }


    public int getCourse_code() {
        return this.course_code;
    }

    public int getStart_date() {
        return this.start_date;
    }

    public String getCourse_name() {
        return this.course_name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public void setCourse_code(int course_code) {
        this.course_code = course_code;
    }

    public void setStart_date(int start_date) {
        this.start_date = start_date;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }







    public String toString() {
        return "Course(course_code=" + this.getCourse_code() + ", start_date=" + this.getStart_date() + ", course_name=" + this.getCourse_name() + ", category=" + this.getCategory() + ", instructor=" + this.getInstructor() + ")";
    }
}
