package registration.dao;

import registration.DatabaseManager;
import registration.model.Student;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO {

    public Student create(Student student) throws SQLException {
        String sql = "INSERT INTO students(name, email) VALUES(?, ?)";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) student.setId(rs.getLong(1));
            }
        }
        return student;
    }

    public Optional<Student> findById(long id) throws SQLException {
        String sql = "SELECT id, name, email FROM students WHERE id = ?";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Student(rs.getLong("id"), rs.getString("name"), rs.getString("email")));
                }
            }
        }
        return Optional.empty();
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT id, name, email FROM students ORDER BY id";
        List<Student> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(rs.getLong("id"), rs.getString("name"), rs.getString("email")));
            }
        }
        return list;
    }
}
