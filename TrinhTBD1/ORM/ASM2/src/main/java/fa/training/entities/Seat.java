package fa.training.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Integer seatId;

    @ManyToOne
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false)
    private CinemaRoom cinemaRoom;

    @Column(name = "SEAT_COLUMN", length = 255)
    private String seatColumn;

    @Column(name = "SEAT_ROW")
    private Integer seatRow;

    @Column(name = "SEAT_STATUS", length = 255)
    private String seatStatus;

    @Column(name = "SEAT_TYPE", length = 255)
    private String seatType;

    public Seat() {
    }

    public Seat(String seatColumn, Integer seatRow, String seatStatus, String seatType) {
        setSeatColumn(seatColumn);
        setSeatRow(seatRow);
        setSeatStatus(seatStatus);
        setSeatType(seatType);
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
        if (seatStatus != null && !seatStatus.equals("Available") && !seatStatus.equals("Not Available") && !seatStatus.equals("Booked")) {
            throw new IllegalArgumentException(new StringBuilder()
                    .append("Invalid seat status: ")
                    .append(seatStatus)
                    .toString());
        }
        this.seatStatus = seatStatus;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        if (seatType != null && !seatType.equals("VIP") && !seatType.equals("Normal")) {
            throw new IllegalArgumentException(new StringBuilder()
                    .append("Invalid seat type: ")
                    .append(seatType)
                    .toString());
        }
        this.seatType = seatType;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Seat{seatId=").append(seatId)
                .append(", seatColumn='").append(seatColumn).append('\'')
                .append(", seatRow=").append(seatRow)
                .append(", seatStatus='").append(seatStatus).append('\'')
                .append(", seatType='").append(seatType).append('\'')
                .append('}')
                .toString();
    }
}
