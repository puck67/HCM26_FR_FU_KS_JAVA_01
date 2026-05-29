package fa.training.entities;

import javax.persistence.*;

@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private int seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false)
    private CinemaRoom cinemaRoom;

    @Column(name = "SEAT_COLUMN", length = 255)
    private String seatColumn;

    @Column(name = "SEAT_ROW")
    private int seatRow;

    @Column(name = "SEAT_STATUS", length = 255)
    private String seatStatus;

    @Column(name = "SEAT_TYPE", length = 255)
    private String seatType;

    public Seat() {}

    public Seat(CinemaRoom cinemaRoom, String seatColumn, int seatRow, String seatStatus, String seatType) {
        this.cinemaRoom = cinemaRoom;
        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
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

    @Override
    public String toString() {
        return new StringBuilder("Seat{")
                .append("id=").append(seatId)
                .append(", col='").append(seatColumn).append('\'')
                .append(", row=").append(seatRow)
                .append(", status='").append(seatStatus).append('\'')
                .append(", type='").append(seatType).append('\'')
                .append('}')
                .toString();
    }
}
