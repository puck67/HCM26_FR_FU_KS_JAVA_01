package fa.training.lms.model;

public class AssessmentMaterial {

    private Long id;
    private String title;
    private String description;
    private String fileName;

    public AssessmentMaterial() {
    }

    public AssessmentMaterial(Long id, String title,
                              String description,
                              String fileName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}