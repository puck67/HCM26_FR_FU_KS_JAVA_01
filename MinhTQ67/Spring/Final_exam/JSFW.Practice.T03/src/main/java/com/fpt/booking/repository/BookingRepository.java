package com.fpt.booking.repository;

import com.fpt.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
           "WHERE (:id IS NULL OR b.id = :id) " +
           "AND (:guestName IS NULL OR LOWER(b.guest.fullName) LIKE LOWER(CONCAT('%', :guestName, '%'))) " +
           "ORDER BY CASE b.status " +
           "WHEN 'CONFIRMED' THEN 1 " +
           "WHEN 'CHECKED_IN' THEN 2 " +
           "WHEN 'CHECKED_OUT' THEN 3 " +
           "WHEN 'CANCELLED' THEN 4 " +
           "ELSE 5 END")
    Page<Booking> searchBookings(@Param("id") Long id, @Param("guestName") String guestName, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.status IN ('CONFIRMED', 'CHECKED_IN') " +
           "AND b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate " +
           "AND b.id != :excludeBookingId")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate,
                                          @Param("excludeBookingId") Long excludeBookingId);
}
