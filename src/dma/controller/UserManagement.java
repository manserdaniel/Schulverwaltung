package dma.controller;

import dma.database.Course;
import dma.database.DatabaseConnector;
import dma.database.Person;

import java.sql.*;
import java.util.LinkedList;

public class UserManagement {

    private final DatabaseConnector dbConnector = DatabaseConnector.getInstance();

    LinkedList<Person> users = new LinkedList<Person>();

    public void getUsersFromDB() {
        String sql;
        ResultSet rs;

        Person tempUser; // is needed because I have the ID on auto_increment

        sql = "SELECT * FROM person";
        rs = dbConnector.fetchData(sql);

        if (users != null) {
            users.clear();
        }

        try {
            while (rs.next()) {
                tempUser = new Person();

                tempUser.setId(rs.getInt("id"));
                tempUser.setFirstName(rs.getString("first_name"));
                tempUser.setLastName(rs.getString("last_name"));
                tempUser.setUsername(rs.getString("username"));
                tempUser.setPassword(rs.getString("password"));
                tempUser.setUserLevel(rs.getInt("user_level_id"));

                users.add(tempUser);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Person userLogin(Person actualUser) {

        Person user = new Person();

        for (Person tempUser : users) {
            if (tempUser.getUsername().equals(actualUser.getUsername()) && tempUser.getPassword().equals(actualUser.getPassword())) {
                user = tempUser;
                break;
            }
        }
        if (user == null) {
            System.out.println("Username oder Passwort falsch");
        }
        return user;
    }

    public void insertNewUser(Person newUser) {
        String sql;

        sql = "INSERT INTO person (first_name, last_name, username, password, user_level_id)\n VALUES ('"
                + newUser.getFirstName() + "',\n '"
                + newUser.getLastName() + "',\n '"
                + newUser.getUsername() + "',\n '"
                + newUser.getPassword() + "',\n "
                + newUser.getUserLevel() + ");";

        dbConnector.update(sql);
    }

    public void printAllUsersOfUserLevel(Integer userLevel) {

        System.out.println("Id - Name");

        for (Person tempUser : users) {
            if (tempUser.getUserLevel() == userLevel) {
                System.out.println(tempUser.getId() + " - " + tempUser.getFirstName() + " " + tempUser.getLastName());
            }
        }
    }
}
