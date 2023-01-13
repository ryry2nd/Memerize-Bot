package memriseAi;

import java.util.HashMap;
import java.util.TreeMap;

import org.openqa.selenium.WebElement;

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

    public static TreeMap<Integer, WebElement> sortbykey(HashMap<Integer, WebElement> map) {
        TreeMap<Integer, WebElement> sorted = new TreeMap<>();
        sorted.putAll(map);
        return sorted;
    }

    public static Boolean isIn(String[] a, String str) {
        for (String s : a) {if (s.equals(str)) {return true;}}
        return false;
    }
}