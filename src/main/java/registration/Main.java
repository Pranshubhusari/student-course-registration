
package registration;

import registration.dao.CourseDAO;
import registration.dao.RegistrationDAO;
import registration.dao.StudentDAO;
import registration.model.Course;
import registration.model.Registration;
import registration.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final RegistrationDAO registrationDAO = new RegistrationDAO();

    public static void main(String[] args) {
        System.out.println("=== Student Course Registration System ===");
        seedSampleDataIfEmpty();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                showMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1": addStudent(sc); break;
                    case "2": listStudents(); break;
                    case "3": addCourse(sc); break;
                    case "4": listCourses(); break;
                    case "5": registerStudentToCourse(sc); break;
                    case "6": viewStudentRegistrations(sc); break;
                    case "7": System.out.println("Exiting..."); return;
                    default: System.out.println("Invalid option.");
                }
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nOptions:");
        System.out.println("1. Add Student");
        System.out.println("2. List Students");
        System.out.println("3. Add Course");
        System.out.println("4. List Courses");
        System.out.println("5. Register Student to Course");
        System.out.println("6. View Student Registrations");
        System.out.println("7. Exit");
        System.out.print("Choose: ");
    }

    private static void addStudent(Scanner sc) {
        try {
            System.out.print("Student name: ");
            String name = sc.nextLine().trim();
            System.out.print("Student email: ");
            String email = sc.nextLine().trim();
            Student s = studentDAO.create(new Student(name, email));
            System.out.println("Created: " + s);
        } catch (SQLException e) {
            System.err.println("Error creating student: " + e.getMessage());
        }
    }

    private static void listStudents() {
        try {
            List<Student> list = studentDAO.findAll();
            if (list.isEmpty()) System.out.println("No students.");
            else list.forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error listing students: " + e.getMessage());
        }
    }

    private static void addCourse(Scanner sc) {
        try {
            System.out.print("Course code (eg. CS101): ");
            String code = sc.nextLine().trim();
            System.out.print("Title: ");
            String title = sc.nextLine().trim();
            System.out.print("Capacity: ");
            int cap = Integer.parseInt(sc.nextLine().trim());
            Course c = courseDAO.create(new Course(code, title, cap));
            System.out.println("Created: " + c);
        } catch (SQLException e) {
            System.err.println("Error creating course: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.err.println("Invalid capacity value.");
        }
    }

    private static void listCourses() {
        try {
            List<Course> list = courseDAO.findAll();
            if (list.isEmpty()) System.out.println("No courses.");
            else {
                for (Course c : list) {
                    int regCount = courseDAO.countRegistrationsForCourse(c.getId());
                    System.out.println(c + " | Registered: " + regCount + "/" + c.getCapacity());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error listing courses: " + e.getMessage());
        }
    }

    private static void registerStudentToCourse(Scanner sc) {
        try {
            System.out.print("Student id: ");
            long sid = Long.parseLong(sc.nextLine().trim());
            System.out.print("Course id: ");
            long cid = Long.parseLong(sc.nextLine().trim());

            Optional<Student> sOpt = studentDAO.findById(sid);
            Optional<Course> cOpt = courseDAO.findById(cid);
            if (sOpt.isEmpty()) { System.out.println("Student not found."); return; }
            if (cOpt.isEmpty()) { System.out.println("Course not found."); return; }

            Course course = cOpt.get();
            int regCount = courseDAO.countRegistrationsForCourse(course.getId());
            if (regCount >= course.getCapacity()) {
                System.out.println("Course is full.");
                return;
            }

            if (registrationDAO.exists(sid, cid)) {
                System.out.println("Student already registered in this course.");
                return;
            }

            Registration r = registrationDAO.create(new Registration(sid, cid));
            System.out.println("Registered successfully: " + r);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id value.");
        } catch (SQLException e) {
            System.err.println("Error registering: " + e.getMessage());
        }
    }

    private static void viewStudentRegistrations(Scanner sc) {
        try {
            System.out.print("Student id: ");
            long sid = Long.parseLong(sc.nextLine().trim());
            Optional<Student> sOpt = studentDAO.findById(sid);
            if (sOpt.isEmpty()) { System.out.println("Student not found."); return; }

            List<Registration> regs = registrationDAO.findByStudentId(sid);
            if (regs.isEmpty()) { System.out.println("No registrations for this student."); return; }
            for (Registration r : regs) {
                System.out.println(r);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id value.");
        } catch (SQLException e) {
            System.err.println("Error fetching registrations: " + e.getMessage());
        }
    }

    private static void seedSampleDataIfEmpty() {
        try {
            if (studentDAO.findAll().isEmpty()) {
                studentDAO.create(new Student("Alice Smith", "alice@example.com"));
                studentDAO.create(new Student("Bob Kumar", "bob@example.com"));
            }
            if (courseDAO.findAll().isEmpty()) {
                courseDAO.create(new Course("CS101", "Intro to Computer Science", 30));
                courseDAO.create(new Course("DS201", "Data Structures", 20));
            }
        } catch (SQLException e) {
            System.err.println("Seeding error: " + e.getMessage());
        }
    }
}