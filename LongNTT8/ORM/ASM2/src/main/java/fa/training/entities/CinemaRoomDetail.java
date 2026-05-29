package fa.training.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CINEMA_ROOM_DETAIL")
public class CinemaRoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_DETAIL_ID")
    private Integer cinemaRoomDetailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINEMA_ROOM_ID")
    private CinemaRoom cinemaRoom;

    @Column(name = "ROOM_RATE")
    private Integer roomRate;

    @Column(name = "ACTIVE_DATE")
    private LocalDate activeDate;

    @Column(name = "ROOM_DESCRIPTION")
    private String roomDescription;

    public CinemaRoomDetail() {
    }

    public CinemaRoomDetail(CinemaRoom cinemaRoom, Integer roomRate, LocalDate activeDate, String roomDescription) {
        this.cinemaRoom = cinemaRoom;
        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.roomDescription = roomDescription;
    }

    public Integer getCinemaRoomDetailId() {
        return cinemaRoomDetailId;
    }

    public void setCinemaRoomDetailId(Integer cinemaRoomDetailId) {
        this.cinemaRoomDetailId = cinemaRoomDetailId;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
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
