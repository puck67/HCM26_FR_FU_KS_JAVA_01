package review.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Song {
    private String id;
    private String title;
    private Authur authurName;
    private String type;
    private int duration;
    private LocalDate releaseDate;

    public Song() {}

    public Song(Authur authurName, int duration, String id, String title, String type, LocalDate releaseDate) {
        this.authurName = authurName;
        this.duration = duration;
        this.id = id;
        this.title = title;
        this.type = type;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Authur getAuthurName() {
        return authurName;
    }

    public void setAuthurName(Authur authurName) {
        this.authurName = authurName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id)
          .append(" | Title: ").append(title)
          .append(" | Author: ").append(authurName.getAuthurName())
          .append(" | Type: ").append(type)
          .append(" | Duration: ").append(duration)
          .append("s | Release Date: ").append(releaseDate.format(formatter));
        return sb.toString();
    }
}
