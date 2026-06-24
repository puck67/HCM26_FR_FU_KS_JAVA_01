package model;

import java.io.Serializable;

public class LmsUser implements Serializable {
    private static final long serialVersionUID = 1L;

    protected long userId;
    protected String name;
    protected String email;

    public LmsUser(long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public void printInfo() {
        System.out.println(new StringBuilder("User [ID=")
                .append(userId).append(", Name=").append(name)
                .append(", Email=").append(email).append("]"));
    }

    @Override
    public String toString() {
        return new StringBuilder("LmsUser [userId=")
                .append(userId).append(", name=").append(name)
                .append(", email=").append(email).append("]").toString();
    }
}
