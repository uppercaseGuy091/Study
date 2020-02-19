package com.lab1.study;

import android.util.Log;
import android.widget.Toast;

import java.sql.*;


public class DbConnection {

    private String url = "jdbc:mysql://remotemysql.com:3306/y8BAQBpFe8?useSSL=false&user=y8BAQBpFe8&password=wcKEPIks8I";
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private static DbConnection instance = new DbConnection();

    private DbConnection() {

    }

    public static DbConnection getInstance() {
        return instance;
    }

    private synchronized void connect() throws SQLException {
        if (connection == null || !connection.isValid(0)) {
            connection = DriverManager.getConnection(url);
        }
        statement = connection.createStatement();

    }

    private void cleanUp() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqlEx) {
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqlEx) {
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException sqlEx) {
            }
        }

    }

    public User logIn(String username, String password) {
        try {
            User user;
            connect();
            preparedStatement = connection.prepareStatement("select * from User where username = ? AND password = ?;");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User(resultSet.getString("username"), resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));

                return user;
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {

            } else {
            }
        } finally {
            cleanUp();
        }

        return null;
    }

}

