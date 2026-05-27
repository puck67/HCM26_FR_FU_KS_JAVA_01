package model;

public class Quiz {
    private String id;
    private String question;
    private String answer;
    private int difficulty; // 1-5
    private String createdBy;

    public Quiz() {}

    public Quiz(String id, String question, String answer, int difficulty, String createdBy) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.difficulty = difficulty;
        this.createdBy = createdBy;
    }

    public String getId()           { return id; }
    public String getQuestion()     { return question; }
    public String getAnswer()       { return answer; }
    public int getDifficulty()      { return difficulty; }
    public String getCreatedBy()    { return createdBy; }

    public void setId(String id)               { this.id = id; }
    public void setQuestion(String question)   { this.question = question; }
    public void setAnswer(String answer)       { this.answer = answer; }
    public void setDifficulty(int difficulty)  { this.difficulty = difficulty; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    @Override
    public String toString() {
        return String.format(
            "| %-8s | %-40s | %-30s | %d/5 | %-15s |",
            id, truncate(question, 40), truncate(answer, 30), difficulty, createdBy
        );
    }

    private String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
}
