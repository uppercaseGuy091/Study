package com.lab1.study;

import android.util.Log;

import java.sql.*;
import java.util.ArrayList;


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
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }

        } catch (SQLException sqlEx) {
            Log.i("SQLException", "Close Exception");
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
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + e.toString());
            }
        } finally {
            cleanUp();
        }

        return null;
    }

    public void addUser(String username, String password, String email) throws Exception {
        try {
            if (validateUsername(username) && validateEmail(email)) {
                connect();
                preparedStatement = connection.prepareStatement("insert into User values(? , ? , ?) ;");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + e.toString());
            }
        } finally {
            cleanUp();
        }
    }

    private boolean validateUsername(String username) throws Exception {
        if (isExisted("User", "username", username)) {
            throw new Exception("This username is taken by another user!");
        }
        return true;
    }

    private boolean validateEmail(String email) throws Exception {
        if ((isExisted("User", "email", email)) && (!email.isEmpty())) {
            throw new Exception("This email is assigned to another user!");
        }
        return true;
    }

    private boolean isExisted(String table, String columnName, String searchedData) {
        try {
            connect();
            preparedStatement = connection.prepareStatement("select " + columnName + " from " + table + " where " + columnName + " = ?  ;");
            preparedStatement.setString(1, searchedData);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + " " + e.toString());
            }
        } finally {
            cleanUp();
        }
        return false;
    }


    public ArrayList<String> fetchSubjects() {

        ArrayList<String> subjects = new ArrayList<>();

        try {
            connect();
            //preparedStatement = connection.prepareStatement("SELECT name FROM Subject");
            //resultSet = preparedStatement.executeQuery();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name FROM Subject");

            while (resultSet.next()) {
                subjects.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + e.toString());
            }
        } finally {
            cleanUp();
            System.out.println("finally");
        }
        return subjects;
    }


    public void addSubject(String name) {

        try {

            if (!isExisted("Subject", "name", name)) {
                connect();
                preparedStatement = connection.prepareStatement("insert into Subject value(?) ;");
                preparedStatement.setString(1, name);
            } else {

            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + e.toString());
            }
        }finally {
            cleanUp();
        }

    }

    public void addDeck(String name) {
        try {

            if (!isExisted("Deck", "name", name)) {
                connect();
                preparedStatement = connection.prepareStatement("insert into Deck value(?) ;");
                preparedStatement.setString(1, name);
            } else {

            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + e.toString());
            }
        }finally {
            cleanUp();
        }
    }

    public ArrayList<String> getDecks(String subjectName){

        ArrayList<String>decks= new ArrayList<>();

        try {
            connect();
            //preparedStatement = connection.prepareStatement("SELECT name FROM Subject");
            //resultSet = preparedStatement.executeQuery();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name FROM Deck where subjectName =" + subjectName);

            while (resultSet.next()) {
                decks.add(resultSet.getString("name"));
            }


        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + e.toString());
            }
        } finally {
            cleanUp();
            System.out.println("finally");
        }
        return decks;


    }





    public void addCard(String text) {
        try {

            if (!isExisted("Card", "text", text)) {
                connect();
                preparedStatement = connection.prepareStatement("insert into Card value(?) ;");
                preparedStatement.setString(1, text);
            } else {

            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1203) {
                Log.i("SQLException", "There is a limited number of available connections");
            } else {
                Log.i("SQLException", e.getErrorCode() + e.toString());
            }
        }finally {
            cleanUp();
        }
    }


}
