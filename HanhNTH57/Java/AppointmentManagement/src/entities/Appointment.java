package entities;

public class Appointment {
    private String id;
    private String person1;
    private String person2;
    private String startTime;
    private String endTime;
    private String place;
    private String reason;

    public Appointment() {
    }

    public Appointment(String id, String person1, String person2, String startTime, String endTime, String place, String reason) {
        this.id = id;
        this.person1 = person1;
        this.person2 = person2;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPerson1() {
        return person1;
    }

    public void setPerson1(String person1) {
        this.person1 = person1;
    }

    public String getPerson2() {
        return person2;
    }

    public void setPerson2(String person2) {
        this.person2 = person2;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Appointment{" + "id=" + id + ", person1=" + person1 + ", person2=" + person2 + ", startTime=" + startTime + ", endTime=" + endTime + ", place=" + place + ", reason=" + reason + '}';
    }
}
