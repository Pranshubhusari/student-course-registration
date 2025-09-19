package registration.dao;

import registration.DatabaseManager;
import registration.model.Registration;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {

    public Registration create(Registration reg) throws SQLException {
        String sql = "INSERT INTO registrations(student_id, course_id) VALUES(?, ?)";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, reg.getStudentId());
            ps.setLong(2, reg.getCourseId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) reg.setId(rs.getLong(1));
            }
        }
        return reg;
    }

    public boolean exists(long studentId, long courseId) throws SQLException {
        String sql = "SELECT 1 FROM registrations WHERE student_id = ? AND course_id = ?";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            ps.setLong(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Registration> findByStudentId(long studentId) throws SQLException {
        String sql = "SELECT id, student_id, course_id, registered_at FROM registrations WHERE student_id = ? ORDER BY registered_at DESC";
        List<Registration> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("registered_at");
                    LocalDateTime dt = ts != null ? ts.toLocalDateTime() : null;
                    Registration r = new Registration(rs.getLong("id"), rs.getLong("student_id"), rs.getLong("course_id"), dt);
                    list.add(r);
                }
            }
        }
        return list;
    }
}
