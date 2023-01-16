package memriseAi;

import java.util.HashMap;



public class BasicFunctions {
    public static String findAns(HashMap<String, String> words, String question) {
        String listQuestion, listAns;
        for (HashMap.Entry<String, String> entry : words.entrySet()) {
            listQuestion = entry.getKey();
            listAns = entry.getValue();
            if (listQuestion.equals(question)) {return listAns;}
            if (listAns.equals(question)) {return listQuestion;}
        }
        return "";
    }
}