package entities;

public class Certificate {

    private String id;
    private String certificateName;
    private String certificateNumber;
    private String issueDate;
    private String expiryDate;
    private double score;
    private String userId;

    public Certificate() {
    }

    public Certificate(String id, String certificateName, String certificateNumber,
                       String issueDate, String expiryDate, double score, String userId) {
        this.id = id;
        this.certificateName = certificateName;
        this.certificateNumber = certificateNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.score = score;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        String reset = utils.Constants.ANSI_RESET;
        String border = utils.Constants.ANSI_BLUE + "|" + reset;

        String fId = String.format("%-6s", id);
        String fName = String.format("%-25s", certificateName);
        String fNumber = String.format("%-18s", certificateNumber);
        String fIssueDate = String.format("%-12s", issueDate);
        String fExpiryDate = String.format("%-12s", expiryDate);
        String fScore = String.format("%-5.2f", score);
        String fUserId = String.format("%-6s", userId);

        return border + " " + utils.Constants.ANSI_BLUE + fId + reset +
               " " + border + " " + fName +
               " " + border + " " + fNumber +
               " " + border + " " + fIssueDate +
               " " + border + " " + fExpiryDate +
               " " + border + " " + utils.Constants.ANSI_PURPLE + fScore + reset +
               " " + border + " " + fUserId +
               " " + border;
    }
}
