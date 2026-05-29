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

    @Column(name = "ROOM_RATE")
    private int roomRate;

    @Column(name = "ACTIVE_DATE")
    private LocalDate activeDate;

    @Column(name = "ROOM_DESCRIPTION")
    private String roomDescription;

    @OneToOne
    @JoinColumn(name = "CINEMA_ROOM_ID")
    private CinemaRoom cinemaRoom;

    public CinemaRoomDetail() {
    }

    public CinemaRoomDetail(int roomRate,
                            LocalDate activeDate,
                            String roomDescription) {

        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.roomDescription = roomDescription;
    }

    // getters setters

    public int getCinemaRoomDetailId() {
        return cinemaRoomDetailId;
    }

    public void setCinemaRoomDetailId(int cinemaRoomDetailId) {
        this.cinemaRoomDetailId = cinemaRoomDetailId;
    }

    public int getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(int roomRate) {
        this.roomRate = roomRate;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public LocalDate getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(LocalDate activeDate) {
        this.activeDate = activeDate;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }
}