package com.fpt.booking.controller;

import com.fpt.booking.dto.BookingForm;
import com.fpt.booking.entity.Booking;
import com.fpt.booking.service.BookingService;
import com.fpt.booking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;

    @GetMapping("/book")
    public String showBookRoomForm(Model model) {
        if (!model.containsAttribute("bookingForm")) {
            model.addAttribute("bookingForm", new BookingForm());
        }
        model.addAttribute("rooms", roomService.findAvailableRooms());
        return "book_room";
    }

    @PostMapping("/book")
    public String submitBooking(@Valid @ModelAttribute("bookingForm") BookingForm form,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", roomService.findAvailableRooms());
            return "book_room";
        }

        try {
            if (form.getId() != null) {
                bookingService.updateBooking(form);
                redirectAttributes.addFlashAttribute("successMessage", "Booking updated successfully!");
                return "redirect:/bookings";
            } else {
                bookingService.createBooking(form);
                model.addAttribute("successMessage", "Booking created successfully!");
                model.addAttribute("bookingForm", new BookingForm());
                model.addAttribute("rooms", roomService.findAvailableRooms());
                return "book_room";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", roomService.findAllRooms()); // if editing, we might need all rooms
            return "book_room";
        }
    }

    @GetMapping("/bookings")
    public String manageBookings(
            @RequestParam(value = "bookingId", required = false) Long bookingId,
            @RequestParam(value = "guestName", required = false) String guestName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Page<Booking> bookingPage = bookingService.searchBookings(bookingId, guestName, PageRequest.of(page, size));

        model.addAttribute("bookingPage", bookingPage);
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("guestName", guestName);
        model.addAttribute("size", size);

        return "manage_bookings";
    }

    @PostMapping("/bookings/checkin/{id}")
    public String checkIn(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.checkIn(id);
            redirectAttributes.addFlashAttribute("successMessage", "Checked in successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/checkout/{id}")
    public String checkOut(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.checkOut(id);
            redirectAttributes.addFlashAttribute("successMessage", "Checked out successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancel(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bookings";
    }

    @GetMapping("/bookings/edit/{id}")
    public String editBooking(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            BookingForm form = bookingService.getBookingForm(id);
            model.addAttribute("bookingForm", form);
            model.addAttribute("rooms", roomService.findAllRooms()); // All rooms can be selected during edit
            return "book_room";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/bookings";
        }
    }
}
