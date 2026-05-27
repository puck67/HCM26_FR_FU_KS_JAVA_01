package model;

import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private String customerId;
    private String roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalAmount;

    public Booking(String bookingId, String customerId, String roomId,
                   LocalDate checkIn, LocalDate checkOut, double totalAmount) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    @Override
    public String toString() {
        return String.format("Booking{id='%s', customer='%s', room='%s', checkIn='%s', checkOut='%s', total=%.2f}",
            bookingId, customerId, roomId, checkIn, checkOut, totalAmount);
    }
}
