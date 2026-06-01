package fa.training.entities;

import javax.persistence.*;

@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Integer seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINEMA_ROOM_ID")
    private CinemaRoom cinemaRoom;

    @Column(name = "SEAT_COLUMN")
    private String seatColumn;

    @Column(name = "SEAT_ROW")
    private Integer seatRow;

    @Column(name = "SEAT_STATUS")
    private String seatStatus;

    @Column(name = "SEAT_TYPE")
    private String seatType;

    public Seat() {
    }

    public Seat(CinemaRoom cinemaRoom, String seatColumn, Integer seatRow, String seatStatus, String seatType) {
        this.cinemaRoom = cinemaRoom;
        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }

    public String getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(String seatColumn) {
        this.seatColumn = seatColumn;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(Integer seatRow) {
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

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", seatColumn='" + seatColumn + '\'' +
                ", seatRow=" + seatRow +
                ", seatStatus='" + seatStatus + '\'' +
                ", seatType='" + seatType + '\'' +
                '}';
    }
}
