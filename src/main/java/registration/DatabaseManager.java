package registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages JDBC connection and initializes schema when using H2 file DB.
 */
public class DatabaseManager {
    // H2 file-based DB (data persisted in project folder ./data/registration_db.mv.db)
    private static final String JDBC_URL = "jdbc:h2:./data/registration_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            initializeSchema();
        } catch (SQLException e) {
            System.err.println("Failed to initialize DB schema: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    private static void initializeSchema() throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // students
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS students (" +
                            "id IDENTITY PRIMARY KEY, " +
                            "name VARCHAR(200) NOT NULL, " +
                            "email VARCHAR(200) NOT NULL UNIQUE" +
                            ");"
            );

            // courses
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS courses (" +
                            "id IDENTITY PRIMARY KEY, " +
                            "code VARCHAR(50) NOT NULL UNIQUE, " +
                            "title VARCHAR(255) NOT NULL, " +
                            "capacity INT NOT NULL" +
                            ");"
            );

            // registrations
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS registrations (" +
                            "id IDENTITY PRIMARY KEY, " +
                            "student_id BIGINT NOT NULL, " +
                            "course_id BIGINT NOT NULL, " +
                            "registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "UNIQUE(student_id, course_id), " +
                            "FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE, " +
                            "FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE" +
                            ");"
            );
        }
    }
}