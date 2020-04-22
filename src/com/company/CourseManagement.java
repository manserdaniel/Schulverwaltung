package com.company;

import java.sql.*;

public class CourseManagement {

    private final DatabaseConnector dbConnector = new DatabaseConnector("jdbc:mysql://localhost:3306/schulverwaltung?user=root");


    public void createCourse(String name, Integer maxAmountSeats, Integer teacherId) {
        String sql;

        sql = "INSERT INTO course (name, max_amount_seats, teacher_id, seats_used) VALUE ('" +
                name + "','" +
                maxAmountSeats + "','" +
                teacherId + "', 0);";

        dbConnector.update(sql);
    }

    public void printCourse() {
        ResultSet rs;
        String sql;

        sql = "SELECT * FROM course";
        rs = dbConnector.fetchData(sql);

        System.out.println("ID - Name - Plätze belegt");

        try {
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                int maxSeats = rs.getInt("max_amount_seats");
                int seatsUsed = rs.getInt("seats_used");
                System.out.println(id + " " + name + " " + seatsUsed + "/" + maxSeats);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void printCourseTeacher(Integer teacherId) {
        ResultSet rs;
        String sql;

        sql = "SELECT id, name, max_amount_seats, seats_used FROM course WHERE teacher_id = '" + teacherId + "';";
        rs = dbConnector.fetchData(sql);

        System.out.println("ID - Name - Plätze belegt");

        try {
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                int maxSeats = rs.getInt("max_amount_seats");
                int seatsUsed = rs.getInt("seats_used");
                System.out.println(id + " " + name + " " + seatsUsed + "/" + maxSeats);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void printCourseTeacherStudents(Integer teacherId, Integer courseId) {
        ResultSet rs;
        String sql;

        sql = "SELECT course_id, name, first_name, last_name FROM student_course " +
                "INNER JOIN person ON person.id = student_course.student_id " +
                "INNER JOIN course on course.id = student_course.course_id " +
                "WHERE course.teacher_id = '" + teacherId + "' AND course_id = '" + courseId + "'";

        rs = dbConnector.fetchData(sql);

        System.out.println("Kurs ID - Kurs - Student");

        try {
            while (rs.next()) {
                Integer id = rs.getInt("course_id");
                String name = rs.getString("name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                System.out.println(id + " - " + name + " - " + firstName + " " + lastName);
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
