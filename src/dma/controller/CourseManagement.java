package dma.controller;

import dma.database.Course;
import dma.database.DatabaseConnector;

import javax.xml.crypto.Data;
import java.security.DrbgParameters;
import java.sql.*;
import java.util.LinkedList;

public class CourseManagement {

    private final DatabaseConnector dbConnector = DatabaseConnector.getInstance();

    LinkedList<Course> courses = new LinkedList<Course>();

    public void getCoursesFromDB() {
        String sql;
        ResultSet rs;

        Course tempCourse; // is needed because I have the ID on auto_increment

        sql = "SELECT * FROM course";
        rs = dbConnector.fetchData(sql);

        if (courses != null) {
            courses.clear();
        }

        try {
            while (rs.next()) {
                tempCourse = new Course(rs.getString("name"), rs.getInt("max_amount_seats"), rs.getInt("teacher_id"));
                tempCourse.setSeatsUsed(rs.getInt("seats_used"));
                tempCourse.setId(rs.getInt("id"));
                courses.add(tempCourse);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertCourse(Course course) {
        String sql;

        sql = "INSERT INTO course (name, max_amount_seats, teacher_id, seats_used) VALUE ('" +
                course.getName() + "','" +
                course.getMaxAmountSeats() + "','" +
                course.getTeacherId() + "', 0);";

        dbConnector.update(sql);
    }

    public void printAllCourses() {

        System.out.println("ID - Name - Plätze belegt");

        for (Course tempCourse : courses) {
            printCourse(tempCourse);
        }
    }

    public void printCourseTeacher(Integer teacherId) {

        System.out.println("ID - Name - Plätze belegt");

        for (Course tempCourse : courses) {
            if (tempCourse.getTeacherId() == teacherId)
                printCourse(tempCourse);
        }
    }

    private void printCourse(Course tempCourse) {
        System.out.println(tempCourse.getId() + " - "
                + tempCourse.getName() + " - "
                + tempCourse.getSeatsUsed() + "/"
                + tempCourse.getMaxAmountSeats());
    }

    public void printCourseTeacherStudents(Integer teacherId, Integer courseId) {
        ResultSet rs;
        String sql;

        sql = "SELECT student_id, name, first_name, last_name FROM student_course " +
                "INNER JOIN person ON person.id = student_course.student_id " +
                "INNER JOIN course on course.id = student_course.course_id " +
                "WHERE course.teacher_id = '" + teacherId + "' AND course_id = '" + courseId + "'";

        rs = dbConnector.fetchData(sql);

        System.out.println("ID - Student");

        try {
            while (rs.next()) {
                Integer id = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                System.out.println(id + " - " + firstName + " " + lastName);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void printStudentCourse(Integer studentId) {
        ResultSet rs;
        String sql;

        sql = "SELECT * from student_course " +
                "INNER JOIN person ON '" + studentId + "' = person.id " +
                "INNER JOIN course ON course.id = student_course.course_id " +
                "WHERE person.id = '" + studentId + "'" +
                "GROUP BY name;";

        rs = dbConnector.fetchData(sql);

        System.out.println("Id - Kurs - Note");

        try {
            while (rs.next()) {
                Integer id = rs.getInt("course_id");
                String name = rs.getString("name");
                int mark = rs.getInt("mark");
                System.out.println(id + " - " + name + " - " + mark);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String signUpForACourse(Integer studentId, Integer courseId) {
        String sql;
        ResultSet rs;

        int seatsUsed = 0;
        int maxAmountSeats = 0;
        String result = "";

        sql = "SELECT seats_used, max_amount_seats FROM course WHERE id = '" + courseId + "';";
        rs = dbConnector.fetchData(sql);

        try {
            while (rs.next()) {
                seatsUsed = rs.getInt("seats_used");
                maxAmountSeats = rs.getInt("max_amount_seats");
            }

            if (seatsUsed < maxAmountSeats) {
                sql = "INSERT INTO student_course (student_id, course_id) VALUE ('" + studentId + "','" + courseId + "');";
                dbConnector.update(sql);

                sql = "UPDATE course SET seats_used = seats_used + 1 WHERE id = '" + courseId + "';";
                dbConnector.update(sql);
                result = "Success";
            } else {
                result = "Keine freien Plätze mehr!";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public String addMarkForStudent(Integer teacherId, Integer studentId, Integer courseId, Integer mark) {
        ResultSet rs;
        String sql;

        int tempTeacherId = 0;

        String result = "";

        sql = "SELECT teacher_id FROM course WHERE id = '" + courseId + "';";
        rs = dbConnector.fetchData(sql);

        try {
            while (rs.next()) {
                tempTeacherId = rs.getInt("teacher_id");
            }

            if (teacherId.equals(tempTeacherId)) {
                if (mark > 0 && mark < 6) {
                    sql = "UPDATE student_course SET mark = '" + mark + "' WHERE course_id = '" + courseId + "' AND student_id = '" + studentId + "';";
                    dbConnector.update(sql);
                    result = "Success";
                } else {
                    result = "Die Note die Sie vergeben möchten ist falsch!";
                }
            } else {
                result = "Sie sind nicht berechtigt diese Note zu vergeben!";
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
