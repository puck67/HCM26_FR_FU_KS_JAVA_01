package fa.training.JSFW_S_A102.model;

import java.util.concurrent.atomic.AtomicLong;

public class AssessmentMaterial {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private long id;
    private String title;
    private String description;
    private String fileName;

    public AssessmentMaterial() {
        this.id = ID_GENERATOR.getAndIncrement();
    }

    public AssessmentMaterial(String title, String description, String fileName) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.title = title;
        this.description = description;
        this.fileName = fileName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
