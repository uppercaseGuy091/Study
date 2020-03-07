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

    public void setUrl(String url) {
        this.url = url;
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
            }else if(e.getErrorCode()==1040){
                setUrl("jdbc:mysql://ricky.heliohost.org:3306/studycar_StudyCards?useSSL=false&user=studycar_StudyCa&password=StudyCards");
                return logIn(username,password);
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


    public void addSubjectToDB(String name) {

        try {

            if (!isExisted("Subject", "name", name)) {
                connect();
                preparedStatement = connection.prepareStatement("insert into Subject value(?) ;");
                preparedStatement.setString(1, name);
                preparedStatement.executeUpdate();
            } else {

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

    public void addDeckToDB(String name, String subjectName) {
        try {

            if (!isExisted("Deck", "name", name)) {
                connect();
                preparedStatement = connection.prepareStatement("insert into Deck (name, subjectName) values(?, ?) ;");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, subjectName);
                preparedStatement.executeUpdate();

            } else {

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


    public void addCardToDB(String question, String answer, int deckId) {
        try {

            connect();
            preparedStatement = connection.prepareStatement("insert into Card (question, answer, deckId) values(?, ?, ?) ;");
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, answer);
            preparedStatement.setInt(3, deckId);
            preparedStatement.executeUpdate();

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


    public ArrayList<Deck> getDecks(String subjectName) {

        ArrayList<Deck> decks = new ArrayList<>();

        try {
            connect();

            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("SELECT name, id FROM Deck where subjectName = '" + subjectName + "'");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Deck deck = new Deck(resultSet.getInt("id"), resultSet.getString("name"));
                decks.add(deck);
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


    public ArrayList<Card> getCards(int deckId) {

        ArrayList<Card> cards = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM Card WHERE deckId = ?");
            preparedStatement.setInt(1, deckId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("question"), resultSet.getString("answer"));
                card.setId(resultSet.getInt("cardId"));
                cards.add(card);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }



    public String getPassword(String email) {
        String password = null;
        try {

            connect();
            preparedStatement = connection.prepareStatement("select password from User where email = ? ;");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                password = resultSet.getString("password");

                return password;
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

        return password;
    }

}
