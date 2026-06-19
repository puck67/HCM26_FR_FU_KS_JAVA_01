package com.fpt.booking.controller;

import com.fpt.booking.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final RoomService roomService;

    @GetMapping("/calculate-price")
    public Map<String, Object> calculatePrice(
            @RequestParam("roomId") Long roomId,
            @RequestParam("checkInDate") String checkInStr,
            @RequestParam("checkOutDate") String checkOutStr) {

        Map<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkInDate = LocalDate.parse(checkInStr, formatter);
            LocalDate checkOutDate = LocalDate.parse(checkOutStr, formatter);

            if (checkInDate.isAfter(checkOutDate)) {
                response.put("error", "Check-out date must be after check-in date");
                return response;
            }

            long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (nights == 0) nights = 1; // minimum 1 night charge? The req says "difference in days". We'll just use the difference.

            Double totalPrice = roomService.calculateTotalPrice(roomId, (int) nights);
            response.put("totalPrice", totalPrice);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
}
