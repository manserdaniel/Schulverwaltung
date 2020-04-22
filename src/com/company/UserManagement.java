package com.company;

import java.sql.*;

public class UserManagement {

    private final DatabaseConnector dbConnector = new DatabaseConnector("jdbc:mysql://localhost:3306/schulverwaltung?user=root");

    public boolean userLogin(String actualUsername, String passwordActualUser, Person actualUser) {
        ResultSet rs;
        String sql;
        String tempPwd = "";

        boolean loginSuccessful = false;

        sql = "SELECT * FROM person WHERE username = '" + actualUsername + "';";
        rs = dbConnector.fetchData(sql);

        try {
            while (rs.next()) {
                tempPwd = rs.getString("password");
                actualUser.setId(rs.getInt("id"));
                actualUser.setFirstName(rs.getString("first_name"));
                actualUser.setLastName(rs.getString("last_name"));
                actualUser.setUsername(rs.getString("username"));
                actualUser.setUserLevel(rs.getInt("user_level_id"));
            }

            if (!tempPwd.equals(passwordActualUser)) {
                System.out.println("Username oder Passwort falsch");
            } else {
                loginSuccessful = true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return loginSuccessful;
    }

    public void addNewUser(Person newUser) {
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
        ResultSet rs;
        String sql;

        sql = "SELECT id, first_name, last_name FROM person " +
                "WHERE user_level_id = '" + userLevel + "'";

        rs = dbConnector.fetchData(sql);

        System.out.println("Id - Name");

        try {
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                System.out.println(id + " - " + firstName + " " + lastName);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
