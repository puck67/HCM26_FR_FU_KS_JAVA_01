package fa.training.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CINEMA_ROOM_DETAIL")
public class CinemaRoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_DETAIL_ID")
    private int cinemaRoomDetailId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false, unique = true)
    private CinemaRoom cinemaRoom;

    @Column(name = "ROOM_RATE", nullable = false)
    private int roomRate;

    @Column(name = "ACTIVE_DATE", nullable = false)
    private LocalDate activeDate;

    @Column(name = "ROOM_DESCRIPTION", length = 250, nullable = false)
    private String roomDescription;

    public CinemaRoomDetail() {}

    public CinemaRoomDetail(CinemaRoom cinemaRoom, int roomRate, LocalDate activeDate, String roomDescription) {
        this.cinemaRoom = cinemaRoom;
        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.roomDescription = roomDescription;
    }

    public int getCinemaRoomDetailId() {
        return cinemaRoomDetailId;
    }

    public void setCinemaRoomDetailId(int cinemaRoomDetailId) {
        this.cinemaRoomDetailId = cinemaRoomDetailId;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }

    public int getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(int roomRate) {
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

    @Override
    public String toString() {
        return "CinemaRoomDetail{id=" + cinemaRoomDetailId + ", roomRate=" + roomRate + ", activeDate=" + activeDate + ", desc='" + roomDescription + "'}";
    }
}
