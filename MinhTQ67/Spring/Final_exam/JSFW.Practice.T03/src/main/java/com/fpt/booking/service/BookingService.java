package com.fpt.booking.service;

import com.fpt.booking.dto.BookingForm;
import com.fpt.booking.entity.Booking;
import com.fpt.booking.enums.BookingStatus;
import com.fpt.booking.entity.Guest;
import com.fpt.booking.entity.Room;
import com.fpt.booking.repository.BookingRepository;
import com.fpt.booking.repository.GuestRepository;
import com.fpt.booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;


    @Transactional
    public void createBooking(BookingForm form) throws Exception {
        validateBookingDates(form);

        Room room = roomRepository.findById(form.getRoomId())
                .orElseThrow(() -> new Exception("Room not found"));

        // Check for double booking
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(room.getId(), form.getCheckInDate(), form.getCheckOutDate(), -1L);
        if (!overlapping.isEmpty()) {
            throw new Exception("The selected room is not available for the given dates.");
        }

        Optional<com.fpt.booking.entity.Guest> existingGuestOpt = guestRepository.findFirstByPhone(form.getPhone());
        Guest guest;
        if (existingGuestOpt.isPresent()) {
            guest = existingGuestOpt.get();
            if (!guest.getFullName().trim().equalsIgnoreCase(form.getCustomerName().trim())) {
                throw new Exception("Phone number is already used by another customer.");
            }
        } else {
            guest = new Guest();
            guest.setPhone(form.getPhone());
            guest.setFullName(form.getCustomerName().trim());
            guest = guestRepository.save(guest);
        }

        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckInDate(form.getCheckInDate());
        booking.setCheckOutDate(form.getCheckOutDate());
        booking.setStatus(BookingStatus.CONFIRMED);

        long nights = ChronoUnit.DAYS.between(form.getCheckInDate(), form.getCheckOutDate());
        double totalPrice = (room.getPrice() * nights) * 1.10;
        booking.setTotalPrice(Math.round(totalPrice * 100.0) / 100.0);

        bookingRepository.save(booking);
    }


    @Transactional
    public void updateBooking(BookingForm form) throws Exception {
        validateBookingDates(form);

        Booking booking = bookingRepository.findById(form.getId())
                .orElseThrow(() -> new Exception("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED && booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new Exception("Only confirmed or checked in bookings can be edited.");
        }

        Room room = roomRepository.findById(form.getRoomId())
                .orElseThrow(() -> new Exception("Room not found"));

        // Check for double booking excluding the current booking ID
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(room.getId(), form.getCheckInDate(), form.getCheckOutDate(), booking.getId());
        if (!overlapping.isEmpty()) {
            throw new Exception("The selected room is not available for the given dates.");
        }

        // Do not allow updating guest info during booking edit
        // Guest guest = booking.getGuest();
        // guest.setFullName(form.getCustomerName());
        // guest.setPhone(form.getPhone());
        // guestRepository.save(guest);

        booking.setRoom(room);
        booking.setCheckInDate(form.getCheckInDate());
        booking.setCheckOutDate(form.getCheckOutDate());

        long nights = ChronoUnit.DAYS.between(form.getCheckInDate(), form.getCheckOutDate());
        double totalPrice = (room.getPrice() * nights) * 1.10;
        booking.setTotalPrice(Math.round(totalPrice * 100.0) / 100.0);

        bookingRepository.save(booking);
    }


    public Page<Booking> searchBookings(Long bookingId, String guestName, Pageable pageable) {
        return bookingRepository.searchBookings(bookingId, guestName, pageable);
    }


    @Transactional
    public void checkIn(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setCheckInDate(java.time.LocalDate.now());
        bookingRepository.save(booking);
    }


    @Transactional
    public void checkOut(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
        booking.setStatus(BookingStatus.CHECKED_OUT);
        booking.setCheckOutDate(java.time.LocalDate.now());
        bookingRepository.save(booking);
    }


    @Transactional
    public void cancel(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }


    public BookingForm getBookingForm(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
        BookingForm form = new BookingForm();
        form.setId(booking.getId());
        form.setCustomerName(booking.getGuest().getFullName());
        form.setPhone(booking.getGuest().getPhone());
        form.setCheckInDate(booking.getCheckInDate());
        form.setCheckOutDate(booking.getCheckOutDate());
        form.setRoomId(booking.getRoom().getId());
        form.setTotalPrice(booking.getTotalPrice());
        return form;
    }

    private void validateBookingDates(BookingForm form) throws Exception {
        if (form.getCheckInDate().isAfter(form.getCheckOutDate())) {
            throw new Exception("Check-out date must be greater than or equal to check-in date.");
        }
    }
}
