package com.plachkovskyy.module2.database;

import com.plachkovskyy.module2.entity.User;
import java.util.ArrayList;
import java.util.List;

public interface DAO {
    void connect();
    void disconnect();
    void createUser(String name);
    void updateUser(String name, int points);
    void removeUser(String name);
    void showAllResults();
    boolean checkUser(String name);
    List<String> getQuestions();
    List<String> getExactQuestions(int[] rowIndex);
    List<String> getAnswers();
    List<String> getExactAnswers(int[] rowIndex);
    List<Integer> getPoints();
    List<Integer> getExactPoints(int[] rowIndex);
    List<User> getUsers();
    int rowsQuestions();
}
