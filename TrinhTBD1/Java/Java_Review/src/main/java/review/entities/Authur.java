package review.entities;

public class Authur {
    private String authurId;
    private String authurName;

    public Authur() {
    }

    public Authur(String authurId, String authurName) {
        this.authurId = authurId;
        this.authurName = authurName;
    }

    public String getAuthurId() {
        return authurId;
    }

    public void setAuthurId(String authurId) {
        this.authurId = authurId;
    }

    public String getAuthurName() {
        return authurName;
    }

    public void setAuthurName(String authurName) {
        this.authurName = authurName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(authurName).append(" (ID: ").append(authurId).append(")");
        return sb.toString();
    }
}
