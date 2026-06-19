package com.fpt.booking.repository;

import com.fpt.booking.entity.Room;
import com.fpt.booking.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus status);
}
