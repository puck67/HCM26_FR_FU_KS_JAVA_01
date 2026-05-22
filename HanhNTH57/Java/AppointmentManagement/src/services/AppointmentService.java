package services;

import entities.Appointment;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    public AppointmentService() {
    }

    public void addAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointments (id, person1, person2, startTime, endTime, place, reason) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, appointment.getId());
            ps.setString(2, appointment.getPerson1());
            ps.setString(3, appointment.getPerson2());
            ps.setString(4, appointment.getStartTime());
            ps.setString(5, appointment.getEndTime());
            ps.setString(6, appointment.getPlace());
            ps.setString(7, appointment.getReason());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding appointment: " + e.getMessage());
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM Appointments";
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Appointment(
                        rs.getString("id"),
                        rs.getString("person1"),
                        rs.getString("person2"),
                        rs.getString("startTime"),
                        rs.getString("endTime"),
                        rs.getString("place"),
                        rs.getString("reason")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
        }
        return list;
    }

    public Appointment findAppointmentById(String id) {
        String sql = "SELECT * FROM Appointments WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Appointment(
                            rs.getString("id"),
                            rs.getString("person1"),
                            rs.getString("person2"),
                            rs.getString("startTime"),
                            rs.getString("endTime"),
                            rs.getString("place"),
                            rs.getString("reason")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointment: " + e.getMessage());
        }
        return null;
    }

    public boolean updateAppointment(Appointment app) {
        String sql = "UPDATE Appointments SET person1 = ?, person2 = ?, startTime = ?, endTime = ?, place = ?, reason = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, app.getPerson1());
            ps.setString(2, app.getPerson2());
            ps.setString(3, app.getStartTime());
            ps.setString(4, app.getEndTime());
            ps.setString(5, app.getPlace());
            ps.setString(6, app.getReason());
            ps.setString(7, app.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAppointment(String id) {
        String sql = "DELETE FROM Appointments WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    public void displayAllAppointments() {
        List<Appointment> list = getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("  No appointments found.");
            return;
        }

        String[] headers = {"ID", "Person1", "Person2", "Start Time", "End Time", "Place", "Reason"};
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) widths[i] = headers[i].length();
        for (Appointment a : list) {
            widths[0] = Math.max(widths[0], a.getId().length());
            widths[1] = Math.max(widths[1], a.getPerson1().length());
            widths[2] = Math.max(widths[2], a.getPerson2().length());
            widths[3] = Math.max(widths[3], a.getStartTime().length());
            widths[4] = Math.max(widths[4], a.getEndTime().length());
            widths[5] = Math.max(widths[5], a.getPlace().length());
            widths[6] = Math.max(widths[6], a.getReason().length());
        }
        StringBuilder borderSb = new StringBuilder("+");
        StringBuilder fmtSb = new StringBuilder("|");
        for (int w : widths) {
            borderSb.append("-").append("-".repeat(w)).append("-+");
            fmtSb.append(" %-").append(w).append("s |");
        }
        String border = borderSb.toString();
        String fmt = fmtSb.toString() + "%n";
        System.out.println(border);
        System.out.printf(fmt, (Object[]) headers);
        System.out.println(border);
        for (Appointment a : list) {
            System.out.printf(fmt, a.getId(), a.getPerson1(), a.getPerson2(),
                    a.getStartTime(), a.getEndTime(), a.getPlace(), a.getReason());
        }
        System.out.println(border);
        System.out.println("  Total: " + list.size() + " appointment(s)");
    }

    public boolean exists(String id) {
        return findAppointmentById(id) != null;
    }


    public boolean isOverlap(String personId, String startTime, String endTime) {
        return isOverlap(personId, startTime, endTime, "");
    }


    public boolean isOverlap(String personId, String startTime, String endTime, String excludeAppId) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE (person1 = ? OR person2 = ?) " +
                     "AND (startTime < ? AND ? < endTime) " +
                     "AND id <> ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, personId);
            ps.setString(2, personId);
            ps.setString(3, endTime);
            ps.setString(4, startTime);
            ps.setString(5, excludeAppId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking overlap: " + e.getMessage());
        }
        return false;
    }
}
