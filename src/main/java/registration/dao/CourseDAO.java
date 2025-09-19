package registration.dao;

import registration.DatabaseManager;
import registration.model.Course;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDAO {

    public Course create(Course course) throws SQLException {
        String sql = "INSERT INTO courses(code, title, capacity) VALUES(?, ?, ?)";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, course.getCode());
            ps.setString(2, course.getTitle());
            ps.setInt(3, course.getCapacity());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) course.setId(rs.getLong(1));
            }
        }
        return course;
    }

    public Optional<Course> findById(long id) throws SQLException {
        String sql = "SELECT id, code, title, capacity FROM courses WHERE id = ?";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Course(rs.getLong("id"), rs.getString("code"), rs.getString("title"), rs.getInt("capacity")));
                }
            }
        }
        return Optional.empty();
    }

    public List<Course> findAll() throws SQLException {
        String sql = "SELECT id, code, title, capacity FROM courses ORDER BY id";
        List<Course> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Course(rs.getLong("id"), rs.getString("code"), rs.getString("title"), rs.getInt("capacity")));
            }
        }
        return list;
    }

    public int countRegistrationsForCourse(long courseId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registrations WHERE course_id = ?";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}
