package com.fpt.booking.service;

import com.fpt.booking.entity.Room;
import com.fpt.booking.enums.RoomStatus;
import com.fpt.booking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;


    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }


    public List<Room> findAvailableRooms() {
        return roomRepository.findByStatus(RoomStatus.AVAILABLE);
    }


    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }


    public Double calculateTotalPrice(Long roomId, int numberOfNights) {
        return roomRepository.findById(roomId).map(room -> {
            double price = (room.getPrice() * numberOfNights) * 1.10; // 10% taxes
            return Math.round(price * 100.0) / 100.0;
        }).orElse(0.0);
    }
}
