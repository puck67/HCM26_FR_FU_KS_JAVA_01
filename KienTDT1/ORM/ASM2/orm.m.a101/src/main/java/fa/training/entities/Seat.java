package fa.training.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private int seatId;

    @Column(name = "SEAT_COLUMN")
    private String seatColumn;

    @Column(name = "SEAT_ROW")
    private int seatRow;

    @Column(name = "SEAT_STATUS")
    private String seatStatus;

    @Column(name = "SEAT_TYPE")
    private String seatType;

    @ManyToOne
    @JoinColumn(name = "CINEMA_ROOM_ID")
    private CinemaRoom cinemaRoom;

    public Seat() {
    }

    public Seat(String seatColumn,
                int seatRow,
                String seatStatus,
                String seatType) {

        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
    }

    // getters setters

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(String seatColumn) {
        this.seatColumn = seatColumn;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public String getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }
}