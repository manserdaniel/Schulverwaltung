package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);

        UserManagement userManagement = new UserManagement();
        CourseManagement courseManagement = new CourseManagement();

        Person actualUser = new Person();
        Person newUser = new Person();

        boolean loginSuccessful;
        boolean programRunning = true;

        while (programRunning) {

            System.out.println("Wilkommen im Schulverwaltungssystem. Bitte melden sie sich mit dem Username und Passwort an: ");

            String actualUsername = scanner.nextLine();
            String actualPassword = scanner.nextLine();

            loginSuccessful = userManagement.userLogin(actualUsername, actualPassword, actualUser);

            while (loginSuccessful) {

                int actualUserLevel = actualUser.getUserLevel();
                int answerUserInt;

                switch (actualUserLevel) {
                    case 1: // Administrator User Level 1
                        System.out.println("Was möchten sie tun?\n" +
                                "1. User hinzufügen\n" +
                                "2. Kurs erstellen\n" +
                                "3. Abmelden\n" +
                                "4. Programm schließen");

                        answerUserInt = scannerInt.nextInt();

                        if (answerUserInt == 1) {
                            createAndAddNewUser(userManagement, newUser);

                        } else if (answerUserInt == 2) {
                            createAndAddNewCourse(userManagement, courseManagement);

                        } else if (answerUserInt == 3) {
                            loginSuccessful = false;

                        } else if (answerUserInt == 4) {
                            loginSuccessful = false;
                            programRunning = false;

                        } else {
                            System.out.println("Eingabe falsch!");
                        }
                        break;

                    case 2: // Teacher User Level 2
                        System.out.println("Was möchten sie tun?\n" +
                                "1. Liste von Kursen die sie unterrichten ausgeben\n" +
                                "2. Liste von Studenten die einen Kurs belegen ausgeben\n" +
                                "3. Note vergeben\n" +
                                "4. Abmelden\n" +
                                "5. Programm schließen");

                        answerUserInt = scannerInt.nextInt();

                        if (answerUserInt == 1) {
                            courseManagement.printCourseTeacher(actualUser.getId());

                        } else if (answerUserInt == 2) {
                            printListOfStudents(courseManagement, actualUser);

                        } else if (answerUserInt == 3) {
                            setMarkForStudent(courseManagement, actualUser);

                        } else if (answerUserInt == 4) {
                            loginSuccessful = false;

                        } else if (answerUserInt == 5) {
                            loginSuccessful = false;
                            programRunning = false;

                        } else {
                            System.out.println("Eingabe falsch!");
                        }
                        break;

                    case 3: // Student User Level 3
                        System.out.println("Was möchten sie tun?\n" +
                                "1. Liste von allen Kursen ausgeben\n" +
                                "2. Für einen Kurs eintragen\n" +
                                "3. Liste der belegten Kurse ausgeben\n" +
                                "4. Abmelden\n" +
                                "5. Programm schließen");

                        answerUserInt = scannerInt.nextInt();

                        if (answerUserInt == 1) {
                            courseManagement.printCourse();

                        } else if (answerUserInt == 2) {

                            selectAndSignUpForACourse(courseManagement, actualUser);

                        }else if (answerUserInt == 3) {
                            courseManagement.printStudentCourse(actualUser.getId());

                        } else if (answerUserInt == 4) {
                            loginSuccessful = false;

                        } else if (answerUserInt == 5) {
                            loginSuccessful = false;
                            programRunning = false;

                        } else {
                            System.out.println("Eingabe falsch!");
                        }
                        break;

                    default:
                        System.out.println("Eingabe falsch!");
                        break;
                }
            }
        }
    }

    private static void selectAndSignUpForACourse(CourseManagement courseManagement, Person actualUser) {
        Scanner scannerInt = new Scanner(System.in);

        System.out.println("Welches Kurs möchten sie gerne belegen? Geben sie die ID ein:");
        Integer courseId = scannerInt.nextInt();
        courseManagement.signUpForACourse(actualUser.getId(), courseId);
    }

    private static void setMarkForStudent(CourseManagement courseManagement, Person actualUser) {
        Scanner scannerInt = new Scanner(System.in);

        System.out.println("Welches Kurs möchten sie benoten? Geben sie die ID ein:");
        courseManagement.printCourseTeacher(actualUser.getId());
        Integer courseId = scannerInt.nextInt();

        System.out.println("Welchen Studenten möchten sie benoten? Geben sie die ID ein:");
        courseManagement.printCourseTeacherStudents(actualUser.getId(), courseId);
        Integer studentId = scannerInt.nextInt();

        System.out.println("Welche Note möchten sie dem Studenten geben?");
        Integer newMark = scannerInt.nextInt();

        System.out.println(courseManagement.addMarkForStudent(actualUser.getId(), studentId, courseId, newMark));
    }

    private static void printListOfStudents(CourseManagement courseManagement, Person actualUser) {
        Scanner scannerInt = new Scanner(System.in);

        System.out.println("Welchen Kurs möchten sie ausgeben? Geben sie die ID ein:");
        courseManagement.printCourseTeacher(actualUser.getId());
        Integer courseId = scannerInt.nextInt();

        courseManagement.printCourseTeacherStudents(actualUser.getId(), courseId);
    }

    private static void createAndAddNewCourse(UserManagement userManagement, CourseManagement courseManagement) {
        Scanner scanner = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);

        System.out.println("Kursname:");
        String newCourseName = scanner.nextLine();

        System.out.println("Freie Plätze:");
        Integer newCourseMaxAmountStudents = scannerInt.nextInt();

        System.out.println("Welcher dieser Lehrer unterrichtet diesen Kurs? Geben sie die ID ein:");

        userManagement.printAllUsersOfUserLevel(2);
        Integer teacherId = scannerInt.nextInt();

        courseManagement.createCourse(newCourseName, newCourseMaxAmountStudents, teacherId);
    }

    private static void createAndAddNewUser(UserManagement userManagement, Person newUser) {
        Scanner scanner = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);

        System.out.println("Vorname:");
        newUser.setFirstName(scanner.nextLine());

        System.out.println("Nachname:");
        newUser.setLastName(scanner.nextLine());

        System.out.println("Username:");
        newUser.setUsername(scanner.nextLine());

        System.out.println("Passwort:");
        newUser.setPassword(scanner.nextLine());

        System.out.println("Userlevel:");
        newUser.setUserLevel(scannerInt.nextInt());

        userManagement.addNewUser(newUser);
    }
}
