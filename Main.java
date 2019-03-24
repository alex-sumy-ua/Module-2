package com.plachkovskyy.module2;

import com.plachkovskyy.module2.database.OracleDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) throws IOException {
//        OracleDAO.getInstance().createUser("Oleg");
//        OracleDAO.getInstance().createUser("Kostya");
//        OracleDAO.getInstance().createUser("Valera");
//        OracleDAO.getInstance().updateUser("Tanya", 43);
//        OracleDAO.getInstance().removeUser("John");
//        OracleDAO.getInstance().getUsers();
//        List<String> allQuestions = OracleDAO.getInstance().getQuestions();
//        List<String> allAnswers = OracleDAO.getInstance().getAnswers();
//        List<Integer> allPoints = OracleDAO.getInstance().getPoints();
//        List<String> questions = new ArrayList<>();
//        List<String> answers = new ArrayList<>();
//        List<Integer> points = new ArrayList<>();

//        for (int i = 0; i < questionsNumber; i++) {
//            questions.add(allQuestions.get(i));
//            answers.add(allAnswers.get(i));
//            points.add(allPoints.get(i));
//        }

        String name = "Noname";         // default value
        int result = 0;                 // initial result
        final int questionsNumber = 5;  // questions quantity

        // create numbers array
        int[] array = new int[OracleDAO.getInstance().rowsQuestions()];
        for (int i = 0; i < array.length; i++) {
            array[i] = i+1;             // VERY IMPORTANT: There is no row number 0 in the table!!!!!!!
        }

        // numbers array shuffling
        Random random = ThreadLocalRandom.current();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }

        // create exact array depending on questionsNumber
        int[] exactArray = new int[questionsNumber];
        System.arraycopy(array, 0, exactArray, 0, questionsNumber);
        List<String> questions = OracleDAO.getInstance().getExactQuestions(exactArray);
        List<String> answers = OracleDAO.getInstance().getExactAnswers(exactArray);
        List<Integer> points = OracleDAO.getInstance().getExactPoints(exactArray);

        // quiz
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Let's begin short quiz...\nPlease, input your name: ");
        String tmp = reader.readLine();
        if (!tmp.isEmpty()) name = tmp;
        for (int i = 0; i < questionsNumber; i++) {
            System.out.println(questions.get(i));
            String answer = reader.readLine();
            if (answer.equals(answers.get(i))) {
                result += points.get(i);
                System.out.println("You are right!");
            } else {
                System.out.println("Sorry, you are wrong!");
            }
        }

        // print results
        if (!OracleDAO.getInstance().checkUser(name)) {
            OracleDAO.getInstance().createUser(name); }
        OracleDAO.getInstance().updateUser(name, result);
        System.out.println("Dear " + name + ". Your result is: " + result + ". It was added to the DataBase...");

        // show all results in case user wants this
        System.out.println("Do you want to have a look at all results? (Y/N): ");
        String want = reader.readLine();
        if (want.equalsIgnoreCase("Y")) {
            OracleDAO.getInstance().showAllResults();
        }

    }
}
