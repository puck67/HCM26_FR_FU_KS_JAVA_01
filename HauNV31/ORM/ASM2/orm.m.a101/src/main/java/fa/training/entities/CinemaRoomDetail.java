package fa.training.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CINEMA_ROOM_DETAIL")
public class CinemaRoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @Column(name = "room_rate", nullable = false)
    private double roomRate;

    // LocalDate for high accuracy date search as per guidelines
    @Column(name = "active_date", nullable = false)
    private LocalDate activeDate;

    @Column(name = "description", length = 500)
    private String description;

    // OneToOne owning side - holds the foreign key
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, unique = true)
    private CinemaRoom cinemaRoom;

    public CinemaRoomDetail() {}

    public CinemaRoomDetail(double roomRate, LocalDate activeDate, String description) {
        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.description = description;
    }

    public Long getDetailId() { return detailId; }
    public void setDetailId(Long detailId) { this.detailId = detailId; }

    public double getRoomRate() { return roomRate; }
    public void setRoomRate(double roomRate) { this.roomRate = roomRate; }

    public LocalDate getActiveDate() { return activeDate; }
    public void setActiveDate(LocalDate activeDate) { this.activeDate = activeDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public CinemaRoom getCinemaRoom() { return cinemaRoom; }
    public void setCinemaRoom(CinemaRoom cinemaRoom) { this.cinemaRoom = cinemaRoom; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CinemaRoomDetail{detailId=").append(detailId)
          .append(", roomRate=").append(roomRate)
          .append(", activeDate=").append(activeDate)
          .append(", description='").append(description).append("'")
          .append("}");
        return sb.toString();
    }
}
