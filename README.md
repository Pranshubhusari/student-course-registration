# Student Course Registration System (Java + JDBC)

A small *console-based* student-course registration system implemented in Java using JDBC and an embedded H2 database .  
This demonstrates: *DAO pattern, SQL schema design, prepared statements, transactions , and simple CLI*.

---

## Features
- Add students and courses
- List students and courses (shows current registration count)
- Register a student to a course with capacity checks
- View registrations for a student
- Uses H2 file-based DB by default (no external DB required)
- Includes db_schema.sql to run on MySQL if desired

---

## Tech stack
- Java 17+
- JDBC
- H2 Database 
- Maven

---

## Project structure
student-course-registration/ ├── pom.xml ├── src/main/java/registration/...  └── README.md
