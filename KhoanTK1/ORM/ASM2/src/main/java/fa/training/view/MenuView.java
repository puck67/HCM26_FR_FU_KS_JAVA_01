package fa.training.view;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.service.RoomDetailService;
import fa.training.service.RoomService;
import fa.training.service.SeatService;
import fa.training.util.InputUtils;
import java.util.List;
import java.util.Optional;

public class MenuView {

    private final RoomService roomService = new RoomService();
    private final RoomDetailService roomDetailService = new RoomDetailService();
    private final SeatService seatService = new SeatService();

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("----- MOVIE THEATER MANAGEMENT -----");
            System.out.println("1. Quan ly phong chieu (Cinema Room)");
            System.out.println("2. Quan ly chi tiet phong (Room Detail)");
            System.out.println("3. Quan ly ghe ngoi (Seat)");
            System.out.println("0. Thoat chuong trinh");
            System.out.println("------------------------------------");

            int choice = InputUtils.readInt("Nhap lua chon cua ban: ");
            switch (choice) {
                case 1:
                    roomMenu();
                    break;
                case 2:
                    detailMenu();
                    break;
                case 3:
                    seatMenu();
                    break;
                case 0:
                    System.out.println("Tam biet! Cam on ban da su dung he thong.");
                    exit = true;
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long nhap tu 0 den 3.");
            }
        }
    }

    // ==========================================
    // CINEMA ROOM MENU
    // ==========================================
    private void roomMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("=== QUAN LY PHONG CHIEU ===");
            System.out.println("1. Hien thi tat ca phong chieu");
            System.out.println("2. Tim phong chieu theo ID");
            System.out.println("3. Them moi phong chieu");
            System.out.println("4. Cap nhat thong tin phong chieu");
            System.out.println("5. Xoa phong chieu");
            System.out.println("0. Quay lai menu chinh");
            System.out.println("===========================");

            int choice = InputUtils.readInt("Nhap lua chon cua ban: ");
            switch (choice) {
                case 1:
                    listRooms();
                    break;
                case 2:
                    findRoomById();
                    break;
                case 3:
                    addRoom();
                    break;
                case 4:
                    updateRoom();
                    break;
                case 5:
                    deleteRoom();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long nhap tu 0 den 5.");
            }
        }
    }

    private void listRooms() {
        System.out.println("\n--- DANH SACH PHONG CHIEU ---");
        List<CinemaRoom> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("Khong co phong chieu nao.");
            return;
        }
        System.out.printf("%-10s %-30s %-15s%n", "Room ID", "Room Name", "Seat Quantity");
        System.out.println("-------------------------------------------------------------");
        for (CinemaRoom r : rooms) {
            System.out.printf("%-10d %-30s %-15d%n", r.getCinemaRoomId(), r.getCinemaRoomName(), r.getSeatQuantity());
        }
        System.out.println("-------------------------------------------------------------");
    }

    private void findRoomById() {
        System.out.println("\n--- TIM KIEM PHONG CHIEU ---");
        int id = InputUtils.readPositiveInt("Nhap ID phong chieu can tim: ");
        Optional<CinemaRoom> roomOpt = roomService.getRoomById(id);
        if (roomOpt.isPresent()) {
            CinemaRoom r = roomOpt.get();
            System.out.println("Thong tin phong chieu:");
            System.out.println(" - ID: " + r.getCinemaRoomId());
            System.out.println(" - Ten phong: " + r.getCinemaRoomName());
            System.out.println(" - So luong ghe: " + r.getSeatQuantity());
        } else {
            System.out.println("Khong tim thay phong chieu voi ID = " + id);
        }
    }

    private void addRoom() {
        System.out.println("\n--- THEM MOI PHONG CHIEU ---");
        String name = InputUtils.readString("Nhap ten phong chieu: ");
        int qty = InputUtils.readPositiveInt("Nhap so luong ghe: ");
        try {
            roomService.addRoom(name, qty);
            System.out.println("Them phong chieu thanh cong!");
        } catch (Exception e) {
            System.out.println("Them that bai! Loi: " + e.getMessage());
        }
    }

    private void updateRoom() {
        System.out.println("\n--- CAP NHAT PHONG CHIEU ---");
        int id = InputUtils.readPositiveInt("Nhap ID phong chieu can sua: ");
        Optional<CinemaRoom> roomOpt = roomService.getRoomById(id);
        if (roomOpt.isEmpty()) {
            System.out.println("Khong tim thay phong chieu voi ID = " + id);
            return;
        }

        System.out.println("Thong tin hien tai: " + roomOpt.get().getCinemaRoomName() + " (" + roomOpt.get().getSeatQuantity() + " ghe)");
        String name = InputUtils.readString("Nhap ten phong moi: ");
        int qty = InputUtils.readPositiveInt("Nhap so luong ghe moi: ");
        try {
            roomService.updateRoom(id, name, qty);
            System.out.println("Cap nhat phong chieu thanh cong!");
        } catch (Exception e) {
            System.out.println("Cap nhat that bai! Loi: " + e.getMessage());
        }
    }

    private void deleteRoom() {
        System.out.println("\n--- XOA PHONG CHIEU ---");
        int id = InputUtils.readPositiveInt("Nhap ID phong chieu muon xoa: ");
        Optional<CinemaRoom> roomOpt = roomService.getRoomById(id);
        if (roomOpt.isEmpty()) {
            System.out.println("Khong tim thay phong chieu voi ID = " + id);
            return;
        }

        String confirm = InputUtils.readString("Ban co chac chan muon xoa phong chieu nay va tat ca ghe ngoi/chi tiet kem theo? (Y/N): ");
        if ("y".equalsIgnoreCase(confirm) || "yes".equalsIgnoreCase(confirm)) {
            try {
                roomService.deleteRoom(id);
                System.out.println("Xoa phong chieu va cac thong tin lien quan thanh cong!");
            } catch (Exception e) {
                System.out.println("Xoa that bai! Loi: " + e.getMessage());
            }
        } else {
            System.out.println("Da huy thao tac xoa.");
        }
    }

    // ==========================================
    // ROOM DETAIL MENU
    // ==========================================
    private void detailMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("=== QUAN LY CHI TIET PHONG ===");
            System.out.println("1. Hien thi tat ca chi tiet phong");
            System.out.println("2. Tim chi tiet phong theo ID");
            System.out.println("3. Tim chi tiet phong theo Room ID");
            System.out.println("4. Them moi chi tiet phong chieu");
            System.out.println("5. Cap nhat chi tiet phong chieu");
            System.out.println("6. Xoa chi tiet phong chieu");
            System.out.println("0. Quay lai menu chinh");
            System.out.println("==============================");

            int choice = InputUtils.readInt("Nhap lua chon cua ban: ");
            switch (choice) {
                case 1:
                    listDetails();
                    break;
                case 2:
                    findDetailById();
                    break;
                case 3:
                    findDetailByRoomId();
                    break;
                case 4:
                    addDetail();
                    break;
                case 5:
                    updateDetail();
                    break;
                case 6:
                    deleteDetail();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long nhap tu 0 den 6.");
            }
        }
    }

    private void listDetails() {
        System.out.println("\n--- DANH SACH CHI TIET PHONG CHIEU ---");
        List<CinemaRoomDetail> details = roomDetailService.getAllDetails();
        if (details.isEmpty()) {
            System.out.println("Khong co chi tiet phong nao.");
            return;
        }
        System.out.printf("%-10s %-10s %-15s %-15s %-30s%n", "Detail ID", "Room ID", "Room Rate", "Active Date", "Description");
        System.out.println("-------------------------------------------------------------------------------------");
        for (CinemaRoomDetail d : details) {
            int rId = d.getCinemaRoom() != null ? d.getCinemaRoom().getCinemaRoomId() : 0;
            System.out.printf("%-10d %-10d %-15d %-15s %-30s%n",
                    d.getCinemaRoomDetailId(), rId, d.getRoomRate(), d.getActiveDate(), d.getRoomDescription());
        }
        System.out.println("-------------------------------------------------------------------------------------");
    }

    private void findDetailById() {
        System.out.println("\n--- TIM CHI TIET PHONG BY ID ---");
        int id = InputUtils.readPositiveInt("Nhap ID chi tiet phong: ");
        Optional<CinemaRoomDetail> detailOpt = roomDetailService.getDetailById(id);
        if (detailOpt.isPresent()) {
            CinemaRoomDetail d = detailOpt.get();
            System.out.println("Thong tin chi tiet phong chieu:");
            System.out.println(" - Detail ID: " + d.getCinemaRoomDetailId());
            System.out.println(" - Room ID: " + (d.getCinemaRoom() != null ? d.getCinemaRoom().getCinemaRoomId() : "N/A"));
            System.out.println(" - Room Rate: " + d.getRoomRate());
            System.out.println(" - Active Date: " + d.getActiveDate());
            System.out.println(" - Description: " + d.getRoomDescription());
        } else {
            System.out.println("Khong tim thay chi tiet phong voi ID = " + id);
        }
    }

    private void findDetailByRoomId() {
        System.out.println("\n--- TIM CHI TIET PHONG BY ROOM ID ---");
        int roomId = InputUtils.readPositiveInt("Nhap ID phong chieu: ");
        Optional<CinemaRoomDetail> detailOpt = roomDetailService.getDetailByRoomId(roomId);
        if (detailOpt.isPresent()) {
            CinemaRoomDetail d = detailOpt.get();
            System.out.println("Thong tin chi tiet cua phong ID = " + roomId + ":");
            System.out.println(" - Detail ID: " + d.getCinemaRoomDetailId());
            System.out.println(" - Room Rate: " + d.getRoomRate());
            System.out.println(" - Active Date: " + d.getActiveDate());
            System.out.println(" - Description: " + d.getRoomDescription());
        } else {
            System.out.println("Khong tim thay ban ghi chi tiet phong cho phong chieu ID = " + roomId);
        }
    }

    private void addDetail() {
        System.out.println("\n--- THEM CHI TIET PHONG CHIEU ---");
        int roomId = InputUtils.readPositiveInt("Nhap ID phong chieu: ");
        int rate = InputUtils.readPositiveInt("Nhap gia phong (Room Rate): ");
        String dateStr = InputUtils.readDate("Nhap ngay kich hoat");
        System.out.print("Nhap mo ta (Description): ");
        String desc = new java.util.Scanner(System.in).nextLine();

        try {
            roomDetailService.addDetail(roomId, rate, dateStr, desc);
            System.out.println("Them chi tiet phong chieu thanh cong!");
        } catch (Exception e) {
            System.out.println("Them that bai! Loi: " + e.getMessage());
        }
    }

    private void updateDetail() {
        System.out.println("\n--- CAP NHAT CHI TIET PHONG CHIEU ---");
        int id = InputUtils.readPositiveInt("Nhap ID chi tiet phong can sua: ");
        Optional<CinemaRoomDetail> detailOpt = roomDetailService.getDetailById(id);
        if (detailOpt.isEmpty()) {
            System.out.println("Khong tim thay chi tiet phong chieu voi ID = " + id);
            return;
        }

        int rate = InputUtils.readPositiveInt("Nhap gia phong moi (Room Rate): ");
        String dateStr = InputUtils.readDate("Nhap ngay kich hoat moi");
        System.out.print("Nhap mo ta moi: ");
        String desc = new java.util.Scanner(System.in).nextLine();

        try {
            roomDetailService.updateDetail(id, rate, dateStr, desc);
            System.out.println("Cap nhat chi tiet phong thanh cong!");
        } catch (Exception e) {
            System.out.println("Cap nhat that bai! Loi: " + e.getMessage());
        }
    }

    private void deleteDetail() {
        System.out.println("\n--- XOA CHI TIET PHONG CHIEU ---");
        int id = InputUtils.readPositiveInt("Nhap ID chi tiet phong muon xoa: ");
        Optional<CinemaRoomDetail> detailOpt = roomDetailService.getDetailById(id);
        if (detailOpt.isEmpty()) {
            System.out.println("Khong tim thay chi tiet phong voi ID = " + id);
            return;
        }

        String confirm = InputUtils.readString("Ban chac chan muon xoa chi tiet phong nay? (Y/N): ");
        if ("y".equalsIgnoreCase(confirm) || "yes".equalsIgnoreCase(confirm)) {
            try {
                roomDetailService.deleteDetail(id);
                System.out.println("Xoa chi tiet phong thanh cong!");
            } catch (Exception e) {
                System.out.println("Xoa that bai! Loi: " + e.getMessage());
            }
        } else {
            System.out.println("Da huy thao tac xoa.");
        }
    }

    // ==========================================
    // SEAT MENU
    // ==========================================
    private void seatMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("=== QUAN LY GHE NGOI ===");
            System.out.println("1. Hien thi toan bo ghe");
            System.out.println("2. Tim ghe theo ID");
            System.out.println("3. Hien thi ghe cua mot phong chieu");
            System.out.println("4. Tim ghe theo trang thai (status)");
            System.out.println("5. Tim ghe theo loai (type)");
            System.out.println("6. Them moi ghe");
            System.out.println("7. Cap nhat thong tin ghe");
            System.out.println("8. Xoa ghe");
            System.out.println("0. Quay lai menu chinh");
            System.out.println("========================");

            int choice = InputUtils.readInt("Nhap lua chon cua ban: ");
            switch (choice) {
                case 1:
                    listSeats();
                    break;
                case 2:
                    findSeatById();
                    break;
                case 3:
                    listSeatsByRoomId();
                    break;
                case 4:
                    findSeatsByStatus();
                    break;
                case 5:
                    findSeatsByType();
                    break;
                case 6:
                    addSeat();
                    break;
                case 7:
                    updateSeat();
                    break;
                case 8:
                    deleteSeat();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long nhap tu 0 den 8.");
            }
        }
    }

    private void listSeats() {
        System.out.println("\n--- DANH SACH TAT CA GHE NGOI ---");
        List<Seat> seats = seatService.getAllSeats();
        printSeatList(seats);
    }

    private void printSeatList(List<Seat> seats) {
        if (seats.isEmpty()) {
            System.out.println("Danh sach ghe trong.");
            return;
        }
        System.out.printf("%-10s %-10s %-10s %-10s %-20s %-15s%n", "Seat ID", "Room ID", "Column", "Row", "Status", "Type");
        System.out.println("-----------------------------------------------------------------------------");
        for (Seat s : seats) {
            int rId = s.getCinemaRoom() != null ? s.getCinemaRoom().getCinemaRoomId() : 0;
            System.out.printf("%-10d %-10d %-10s %-10d %-20s %-15s%n",
                    s.getSeatId(), rId, s.getSeatColumn(), s.getSeatRow(), s.getSeatStatus(), s.getSeatType());
        }
        System.out.println("-----------------------------------------------------------------------------");
    }

    private void findSeatById() {
        System.out.println("\n--- TIM KIEM GHE BANG ID ---");
        int id = InputUtils.readPositiveInt("Nhap ID ghe ngoi: ");
        Optional<Seat> seatOpt = seatService.getSeatById(id);
        if (seatOpt.isPresent()) {
            Seat s = seatOpt.get();
            System.out.println("Thong tin ghe:");
            System.out.println(" - Seat ID: " + s.getSeatId());
            System.out.println(" - Room ID: " + (s.getCinemaRoom() != null ? s.getCinemaRoom().getCinemaRoomId() : "N/A"));
            System.out.println(" - Column: " + s.getSeatColumn());
            System.out.println(" - Row: " + s.getSeatRow());
            System.out.println(" - Status: " + s.getSeatStatus());
            System.out.println(" - Type: " + s.getSeatType());
        } else {
            System.out.println("Khong tim thay ghe voi ID = " + id);
        }
    }

    private void listSeatsByRoomId() {
        System.out.println("\n--- DANH SACH GHE THEO PHONG CHIEU ---");
        int roomId = InputUtils.readPositiveInt("Nhap ID phong chieu: ");
        List<Seat> seats = seatService.getSeatsByRoomId(roomId);
        printSeatList(seats);
    }

    private void findSeatsByStatus() {
        System.out.println("\n--- TIM KIEM GHE THEO TRANG THAI ---");
        String status = InputUtils.readSeatStatus("Nhap trang thai");
        List<Seat> seats = seatService.getSeatsByStatus(status);
        printSeatList(seats);
    }

    private void findSeatsByType() {
        System.out.println("\n--- TIM KIEM GHE THEO LOAI ---");
        String type = InputUtils.readSeatType("Nhap loai ghe");
        List<Seat> seats = seatService.getSeatsByType(type);
        printSeatList(seats);
    }

    private void addSeat() {
        System.out.println("\n--- THEM MOI GHE NGOI ---");
        int roomId = InputUtils.readPositiveInt("Nhap ID phong chieu: ");
        String col = InputUtils.readString("Nhap ky tu cot (vi du A, B, C): ");
        int row = InputUtils.readPositiveInt("Nhap hang so (Row): ");
        String status = InputUtils.readSeatStatus("Nhap trang thai ghe");
        String type = InputUtils.readSeatType("Nhap loai ghe");

        try {
            seatService.addSeat(roomId, col, row, status, type);
            System.out.println("Them ghe moi thanh cong!");
        } catch (Exception e) {
            System.out.println("Them that bai! Loi: " + e.getMessage());
        }
    }

    private void updateSeat() {
        System.out.println("\n--- CAP NHAT GHE NGOI ---");
        int seatId = InputUtils.readPositiveInt("Nhap ID ghe can sua: ");
        Optional<Seat> seatOpt = seatService.getSeatById(seatId);
        if (seatOpt.isEmpty()) {
            System.out.println("Khong tim thay ghe voi ID = " + seatId);
            return;
        }

        int roomId = InputUtils.readPositiveInt("Nhap ID phong chieu moi: ");
        String col = InputUtils.readString("Nhap ky tu cot moi: ");
        int row = InputUtils.readPositiveInt("Nhap hang so (Row) moi: ");
        String status = InputUtils.readSeatStatus("Nhap trang thai moi");
        String type = InputUtils.readSeatType("Nhap loai ghe moi");

        try {
            seatService.updateSeat(seatId, roomId, col, row, status, type);
            System.out.println("Cap nhat thong tin ghe thanh cong!");
        } catch (Exception e) {
            System.out.println("Cap nhat that bai! Loi: " + e.getMessage());
        }
    }

    private void deleteSeat() {
        System.out.println("\n--- XOA GHE NGOI ---");
        int id = InputUtils.readPositiveInt("Nhap ID ghe muon xoa: ");
        Optional<Seat> seatOpt = seatService.getSeatById(id);
        if (seatOpt.isEmpty()) {
            System.out.println("Khong tim thay ghe voi ID = " + id);
            return;
        }

        String confirm = InputUtils.readString("Ban co chac muon xoa ghe nay? (Y/N): ");
        if ("y".equalsIgnoreCase(confirm) || "yes".equalsIgnoreCase(confirm)) {
            try {
                seatService.deleteSeat(id);
                System.out.println("Xoa ghe ngoi thanh cong!");
            } catch (Exception e) {
                System.out.println("Xoa that bai! Loi: " + e.getMessage());
            }
        } else {
            System.out.println("Da huy thao tac xoa.");
        }
    }
}
