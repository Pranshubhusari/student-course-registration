package registration.model;

import java.time.LocalDateTime;

public class Registration {
    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDateTime registeredAt;

    public Registration() {}

    public Registration(Long id, Long studentId, Long courseId, LocalDateTime registeredAt) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.registeredAt = registeredAt;
    }

    public Registration(Long studentId, Long courseId) {
        this(null, studentId, courseId, null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    @Override
    public String toString() {
        return "Registration{" + "id=" + id + ", studentId=" + studentId + ", courseId=" + courseId + ", registeredAt=" + registeredAt + '}';
    }
}
