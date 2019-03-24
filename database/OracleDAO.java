package com.plachkovskyy.module2.database;

import com.plachkovskyy.module2.entity.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleDAO implements DAO {

    private static final OracleDAO instance = new OracleDAO();
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private OracleDAO() {}

    public static OracleDAO getInstance() {
        return instance;
    }

    @Override
    public void connect() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection =
             DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "alex-ua", "prpf+911");
//            if (!connection.isClosed()) System.out.println("Connected successfully");
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null) { connection.close(); }
            if (preparedStatement != null) { preparedStatement.close(); }
            if (resultSet != null) { resultSet.close(); }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void createUser(String name) {
        connect();
        try {
            preparedStatement =
              connection.prepareStatement("INSERT INTO USERS (ID, NAME, MAX_TOTAL_POINTS) values (null, ?, null )");
            preparedStatement.setString(1, name);
            //preparedStatement.setInt(2, 0);
            preparedStatement.execute();
//            System.out.println("Data adding was ok: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void updateUser(String name, int points) {
        connect();
        try {
            preparedStatement = connection.prepareStatement("UPDATE USERS SET MAX_TOTAL_POINTS = ? WHERE NAME = ?");
            preparedStatement.setInt(1, points);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
//            System.out.println("Updating data was ok...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void removeUser(String name) {
        connect();
        try {
            preparedStatement = connection.prepareStatement("DELETE USERS WHERE NAME = ?");
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            System.out.println("Removing data was ok...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void showAllResults() {
        connect();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM USERS ORDER BY ID");
            ResultSet resultSet = preparedStatement.executeQuery();
            int numCols = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
//                StringBuilder sb = new StringBuilder();
//                for (int i = 1; i <= numCols; i++) {
//                    sb.append(String.format(String.valueOf(resultSet.getString(i))) + " ");
//                }
//                System.out.println(sb.toString());
                System.out.println(parseUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public boolean checkUser(String name) {
        connect();
        boolean exists = false;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM USERS WHERE NAME = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            int recordCount = 0;
            while(resultSet.next()) { recordCount++; }
            if (recordCount > 0) { exists = true; }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return exists;
    }

    @Override
    public int rowsQuestions() {
        int quantity = 0;
        connect();
        try {
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM QUESTIONS");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) quantity = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return quantity;
    }

    @Override
    public List<String> getQuestions() {
        List<String> list = new ArrayList<>();
        connect();
        try {
            preparedStatement = connection.prepareStatement("SELECT QUESTION FROM QUESTIONS");
            ResultSet resultSet = preparedStatement.executeQuery();
            int numCols = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= numCols; i++) {
                    sb.append(String.format(String.valueOf(resultSet.getString(i)))/* + " "*/);
                }
                list.add(sb.toString());
            }
//            System.out.println("execute done...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return list;
    }

    @Override
    public List<String> getExactQuestions(int[] rowIndex) {
        List<String> list = new ArrayList<>();
        connect();
        try {
            for (int i = 0; i < rowIndex.length; i++) {
                preparedStatement = connection.prepareStatement("SELECT ID, QUESTION FROM QUESTIONS WHERE ID = ?");
                preparedStatement.setInt(1, rowIndex[i]);
                ResultSet resultSet = preparedStatement.executeQuery();
//                StringBuilder sb = new StringBuilder();
//                sb.append(String.format(String.valueOf(resultSet.getString(2))));
//                list.add(sb.toString());
                if (resultSet.next()) list.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return list;
    }

    @Override
    public List<String> getAnswers() {
        List<String> list = new ArrayList<>();
        connect();
        try {
            preparedStatement = connection.prepareStatement("SELECT ANSWER FROM QUESTIONS");
            ResultSet resultSet = preparedStatement.executeQuery();
            int numCols = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= numCols; i++) {
                    sb.append(String.format(String.valueOf(resultSet.getString(i)))/* + " "*/);
                }
                list.add(sb.toString());
            }
//            System.out.println("execute done...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return list;
    }

    @Override
    public List<String> getExactAnswers(int[] rowIndex) {
        List<String> list = new ArrayList<>();
        connect();
        try {
            for (int i = 0; i < rowIndex.length; i++) {
                preparedStatement = connection.prepareStatement("SELECT ID, ANSWER FROM QUESTIONS WHERE ID = ?");
                preparedStatement.setInt(1, rowIndex[i]);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) list.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return list;
    }

    @Override
    public List<Integer> getPoints() {
        List<Integer> list = new ArrayList<>();
        connect();
        try {
            preparedStatement = connection.prepareStatement("SELECT POINTS FROM QUESTIONS");
            ResultSet resultSet = preparedStatement.executeQuery();
            int numCols = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }
//            System.out.println("execute done...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return list;
    }

    @Override
    public List<Integer> getExactPoints(int[] rowIndex) {
        List<Integer> list = new ArrayList<>();
        connect();
        try {
            for (int i = 0; i < rowIndex.length; i++) {
                preparedStatement = connection.prepareStatement("SELECT ID, POINTS FROM QUESTIONS WHERE ID = ?");
                preparedStatement.setInt(1, rowIndex[i]);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) list.add(resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return list;
    }


    @Override
    public List<User> getUsers() {
        List<User> result = new ArrayList<>();
        connect();
        try {
            preparedStatement = connection.prepareStatement("SELECT * from USERS ORDER BY NAME");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(parseUser(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());
        disconnect();
        return result;
    }

    private User parseUser(ResultSet resultSet) {
        User result = null;
        try {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            int points = resultSet.getInt("MAX_TOTAL_POINTS");
            result = new User(id, name, points);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
