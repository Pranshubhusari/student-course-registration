package registration.model;

public class Course {
    private Long id;
    private String code;
    private String title;
    private int capacity;

    public Course() {}

    public Course(Long id, String code, String title, int capacity) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.capacity = capacity;
    }

    public Course(String code, String title, int capacity) {
        this(null, code, title, capacity);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", code='" + code + '\'' + ", title='" + title + '\'' + ", capacity=" + capacity + '}';
    }
}
