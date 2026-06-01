package fa.training.main;

import fa.training.config.HibernateUtils;
import fa.training.entities.CinemaRoom;
import fa.training.entities.Seat;
import fa.training.service.RoomService;
import fa.training.service.RoomDetailService;
import fa.training.service.SeatService;
import fa.training.utils.InputUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MovieTheaterApp {

    private static final RoomService roomService = new RoomService();
    private static final RoomDetailService detailService = new RoomDetailService();
    private static final SeatService seatService = new SeatService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- Khởi tạo cấu hình Movie Theater Database ---");
        try {
            HibernateUtils.getSessionFactory(); // Kích hoạt tạo bảng tự động
            initSampleData(); // Tự động khởi tạo dữ liệu mẫu
        } catch (Exception e) {
            System.err.println("Không thể kết nối đến PostgreSQL. Hãy đảm bảo bạn đã tạo database 'movietheaterdb' và service PostgreSQL đang chạy.");
            e.printStackTrace();
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputUtils.readIntInput(scanner, "Mời bạn chọn chức năng: ");
            
            // Sử dụng Lambda Switch Case (Java 14+)
            switch (choice) {
                case 1 -> createCinemaRoom();
                case 2 -> viewAllRooms();
                case 3 -> addRoomDetail();
                case 4 -> addSeatToRoom();
                case 5 -> viewAllSeats();
                case 6 -> {
                    System.out.println("Đang tắt ứng dụng và đóng kết nối...");
                    HibernateUtils.shutdown();
                    running = false;
                }
                default -> System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại từ 1 đến 6.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========= MOVIE THEATER APPLICATION =========");
        System.out.println("1. Thêm mới Phòng Chiếu (Cinema Room)");
        System.out.println("2. Xem danh sách Phòng Chiếu");
        System.out.println("3. Thêm Chi tiết Phòng Chiếu (Room Detail)");
        System.out.println("4. Thêm Ghế vào Phòng (Add Seat)");
        System.out.println("5. Xem danh sách toàn bộ Ghế");
        System.out.println("6. Thoát chương trình");
        System.out.println("=============================================");
    }

    // CHỨC NĂNG 1: Tạo phòng chiếu mới
    private static void createCinemaRoom() {
        System.out.println("\n--- Thêm mới Phòng Chiếu ---");
        String name = InputUtils.readStringInput(scanner, "Nhập tên phòng chiếu: ");
        int quantity = InputUtils.readPositiveIntInput(scanner, "Nhập số lượng ghế: ");

        roomService.createRoom(name, quantity);
        System.out.println("Thêm phòng chiếu thành công!");
    }

    // CHỨC NĂNG 2: Xem danh sách phòng
    private static void viewAllRooms() {
        System.out.println("\n--- Danh sách Phòng Chiếu ---");
        List<CinemaRoom> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("Hiện chưa có phòng chiếu nào.");
            return;
        }
        for (CinemaRoom r : rooms) {
            System.out.printf("ID: %d | Tên: %s | Số lượng ghế quy định: %d\n", 
                    r.getCinemaRoomId(), r.getCinemaRoomName(), r.getSeatQuantity());
        }
    }

    // CHỨC NĂNG 3: Thêm chi tiết cho phòng chiếu (1-1)
    private static void addRoomDetail() {
        System.out.println("\n--- Thêm Chi tiết Phòng Chiếu ---");
        viewAllRooms();
        int roomId = InputUtils.readIntInput(scanner, "Chọn ID Phòng cần thêm chi tiết: ");
        CinemaRoom room = roomService.getRoomById(roomId);

        if (room == null) {
            System.out.println("Không tìm thấy phòng chiếu với ID này!");
            return;
        }
        if (room.getCinemaRoomDetail() != null) {
            System.out.println("Phòng chiếu này đã có thông tin chi tiết!");
            return;
        }

        int rate = InputUtils.readPositiveIntInput(scanner, "Nhập giá phòng (Room Rate): ");
        LocalDate activeDate = InputUtils.readDateInput(scanner, "Nhập ngày kích hoạt (yyyy-MM-dd): ");
        String description = InputUtils.readStringInput(scanner, "Nhập mô tả phòng: ");

        detailService.addRoomDetail(rate, activeDate, description, room);
        System.out.println("Thêm chi tiết phòng thành công!");
    }

    // CHỨC NĂNG 4: Thêm ghế vào phòng chiếu (1-N)
    private static void addSeatToRoom() {
        System.out.println("\n--- Thêm Ghế Vào Phòng ---");
        viewAllRooms();
        int roomId = InputUtils.readIntInput(scanner, "Chọn ID Phòng cần thêm ghế: ");
        CinemaRoom room = roomService.getRoomById(roomId);

        if (room == null) {
            System.out.println("Không tìm thấy phòng chiếu!");
            return;
        }

        String column = InputUtils.readStringInput(scanner, "Nhập cột ghế (Ví dụ: A, B, C): ");
        int row = InputUtils.readPositiveIntInput(scanner, "Nhập hàng ghế (Số nguyên): ");
        
        String status = InputUtils.readValidatedStatusInput(scanner); // Validate trạng thái 'Available', 'Not Available', 'Booked'
        String type = InputUtils.readValidatedTypeInput(scanner);     // Validate loại ghế 'VIP', 'Normal'

        seatService.addSeat(column, row, status, type, room);
        System.out.println("Thêm ghế vào phòng thành công!");
    }

    // CHỨC NĂNG 5: Xem danh sách ghế
    private static void viewAllSeats() {
        System.out.println("\n--- Toàn bộ danh sách ghế ---");
        List<Seat> seats = seatService.getAllSeats();
        if (seats.isEmpty()) {
            System.out.println("Hiện không có ghế nào trong hệ thống.");
            return;
        }
        for (Seat s : seats) {
            System.out.printf("Seat ID: %d | Vị trí: Hàng %d-Cột %s | Trạng thái: %s | Loại: %s | Thuộc phòng: %s\n",
                    s.getSeatId(), s.getSeatRow(), s.getSeatColumn(), s.getSeatStatus(), s.getSeatType(), s.getCinemaRoom().getCinemaRoomName());
        }
    }

    // TỰ ĐỘNG KHỞI TẠO DỮ LIỆU MẪU (MOCK DATA)
    private static void initSampleData() {
        List<CinemaRoom> existingRooms = roomService.getAllRooms();
        boolean hasSample = existingRooms.stream().anyMatch(r -> r.getCinemaRoomName().equals("Room 01 - IMAX"));
        if (hasSample) {
            return; // Đã khởi tạo dữ liệu mẫu trước đó, không ghi đè
        }

        System.out.println("Chưa có dữ liệu mẫu. Đang tự động khởi tạo dữ liệu mẫu...");

        // 1. Tạo 3 phòng chiếu
        roomService.createRoom("Room 01 - IMAX", 50);
        roomService.createRoom("Room 02 - Standard", 40);
        roomService.createRoom("Room 03 - Deluxe VIP", 20);

        // Lấy lại các phòng từ DB để liên kết các quan hệ
        List<CinemaRoom> savedRooms = roomService.getAllRooms();
        CinemaRoom dbRoom1 = savedRooms.stream().filter(r -> r.getCinemaRoomName().equals("Room 01 - IMAX")).findFirst().orElse(null);
        CinemaRoom dbRoom2 = savedRooms.stream().filter(r -> r.getCinemaRoomName().equals("Room 02 - Standard")).findFirst().orElse(null);
        CinemaRoom dbRoom3 = savedRooms.stream().filter(r -> r.getCinemaRoomName().equals("Room 03 - Deluxe VIP")).findFirst().orElse(null);

        // 2. Tạo chi tiết phòng (Mối quan hệ 1-1)
        if (dbRoom1 != null) {
            detailService.addRoomDetail(150000, LocalDate.of(2026, 1, 1), "Phòng chiếu IMAX màn hình cong cực đại", dbRoom1);
        }
        if (dbRoom2 != null) {
            detailService.addRoomDetail(90000, LocalDate.of(2026, 2, 1), "Phòng tiêu chuẩn âm thanh Dolby 7.1", dbRoom2);
        }
        if (dbRoom3 != null) {
            detailService.addRoomDetail(250000, LocalDate.of(2026, 3, 1), "Phòng sang trọng với ghế nằm massage", dbRoom3);
        }

        // 3. Tạo một số ghế mẫu (Mối quan hệ 1-Many)
        if (dbRoom1 != null) {
            seatService.addSeat("A", 1, "Available", "Normal", dbRoom1);
            seatService.addSeat("A", 2, "Available", "Normal", dbRoom1);
            seatService.addSeat("B", 5, "Booked", "VIP", dbRoom1);
            seatService.addSeat("B", 6, "Available", "VIP", dbRoom1);
        }
        if (dbRoom2 != null) {
            seatService.addSeat("C", 3, "Available", "Normal", dbRoom2);
            seatService.addSeat("C", 4, "Not Available", "Normal", dbRoom2);
            seatService.addSeat("D", 7, "Available", "VIP", dbRoom2);
        }
        if (dbRoom3 != null) {
            seatService.addSeat("E", 1, "Available", "VIP", dbRoom3);
            seatService.addSeat("E", 2, "Booked", "VIP", dbRoom3);
        }

        System.out.println("Tự động khởi tạo dữ liệu mẫu thành công!");
    }
}
