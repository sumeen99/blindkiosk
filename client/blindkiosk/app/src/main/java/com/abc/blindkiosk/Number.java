package com.abc.blindkiosk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Number {
    Set<String> one = new HashSet<String>();
    Set<String> two = new HashSet<String>();
    Set<String> three = new HashSet<String>();
    Set<String> four = new HashSet<String>();
    Set<String> five = new HashSet<String>();
    Map<String, Set<String>> numberMap = new HashMap<String, Set<String>>();
    Number(){
        one.add("1");
        one.add("첫");
        one.add("일");
        numberMap.put("1",one);

        two.add("2");
        two.add("두");
        two.add("이");
        numberMap.put("2",two);

        three.add("3");
        three.add("세");
        three.add("삼");
        numberMap.put("3",three);

        four.add("4");
        four.add("네");
        four.add("사");
        numberMap.put("4",four);

        five.add("5");
        five.add("다섯");
        five.add("오");
        numberMap.put("5",five);
    }

    String findNumber(String answer){
        Set<String> keySet = numberMap.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while(keyIterator.hasNext()){
            String key = keyIterator.next();
            Set<String> number = numberMap.get(key);
            Iterator<String> numberIterator = number.iterator();
            while(numberIterator.hasNext()){
                String numberStr = numberIterator.next();
                if (answer.contains(numberStr)){
                    return key;
                }
            }
        }
        return null;
    }

}
