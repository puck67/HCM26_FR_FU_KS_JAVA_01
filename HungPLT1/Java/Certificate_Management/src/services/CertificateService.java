package services;

import entities.Certificate;
import utils.Constants;
import utils.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CertificateService {

    public boolean addCertificate(Certificate cert) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(Constants.SP_INSERT_CERTIFICATE)) {

            stmt.setString(1, cert.getId());
            stmt.setString(2, cert.getCertificateName());
            stmt.setString(3, cert.getCertificateNumber());
            stmt.setString(4, cert.getIssueDate());
            stmt.setString(5, cert.getExpiryDate());
            stmt.setDouble(6, cert.getScore());
            stmt.setString(7, cert.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Certificate> getAllCertificates() {
        List<Certificate> list = new ArrayList<Certificate>();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(Constants.SP_GET_ALL_CERTIFICATES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Certificate cert = new Certificate(
                        rs.getString("id"),
                        rs.getString("certificate_name"),
                        rs.getString("certificate_number"),
                        utils.Validator.normalizeDate(rs.getString("issue_date")),
                        utils.Validator.normalizeDate(rs.getString("expiry_date")),
                        rs.getDouble("score"),
                        rs.getString("user_id")
                );
                list.add(cert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateCertificate(Certificate cert) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(Constants.SP_UPDATE_CERTIFICATE)) {

            stmt.setString(1, cert.getId());
            stmt.setString(2, cert.getCertificateName());
            stmt.setString(3, cert.getCertificateNumber());
            stmt.setString(4, cert.getIssueDate());
            stmt.setString(5, cert.getExpiryDate());
            stmt.setDouble(6, cert.getScore());
            stmt.setString(7, cert.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCertificate(String id) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(Constants.SP_DELETE_CERTIFICATE)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Certificate findById(String id) {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(Constants.SP_FIND_CERTIFICATE_BY_ID)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Certificate(
                            rs.getString("id"),
                            rs.getString("certificate_name"),
                            rs.getString("certificate_number"),
                            utils.Validator.normalizeDate(rs.getString("issue_date")),
                            utils.Validator.normalizeDate(rs.getString("expiry_date")),
                            rs.getDouble("score"),
                            rs.getString("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
