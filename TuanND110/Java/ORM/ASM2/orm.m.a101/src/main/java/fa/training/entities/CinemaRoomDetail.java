package fa.training.entities;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * CinemaRoomDetail Entity - Contains detailed information about a cinema room
 */
@Entity
@Table(name = "cinema_room_detail")
public class CinemaRoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_room_detail_id")
    private Integer cinemaRoomDetailId;

    @Column(name = "room_rate", nullable = false)
    private Integer roomRate;

    @Column(name = "active_date", nullable = false)
    private LocalDate activeDate;

    @Column(name = "room_description", nullable = false, length = 250)
    private String roomDescription;

    @OneToOne(optional = false)
    @JoinColumn(name = "cinema_room_id", referencedColumnName = "cinema_room_id", nullable = false, unique = true)
    private CinemaRoom cinemaRoom;

    // Constructors
    public CinemaRoomDetail() {
    }

    public CinemaRoomDetail(Integer roomRate, LocalDate activeDate, String roomDescription) {
        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.roomDescription = roomDescription;
    }

    // Getters and Setters
    public Integer getCinemaRoomDetailId() {
        return cinemaRoomDetailId;
    }

    public void setCinemaRoomDetailId(Integer cinemaRoomDetailId) {
        this.cinemaRoomDetailId = cinemaRoomDetailId;
    }

    public Integer getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(Integer roomRate) {
        this.roomRate = roomRate;
    }

    public LocalDate getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(LocalDate activeDate) {
        this.activeDate = activeDate;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }

    @Override
    public String toString() {
        return "CinemaRoomDetail{" +
                "cinemaRoomDetailId=" + cinemaRoomDetailId +
                ", roomRate=" + roomRate +
                ", activeDate=" + activeDate +
                ", roomDescription='" + roomDescription + '\'' +
                '}';
    }
}
