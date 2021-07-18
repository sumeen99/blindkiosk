package com.abc.blindkiosk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Number {
    Set<String> zero = new HashSet<String>();
    Set<String> one = new HashSet<String>();
    Set<String> two = new HashSet<String>();
    Set<String> three = new HashSet<String>();
    Set<String> four = new HashSet<String>();
    Set<String> five = new HashSet<String>();
    Map<String, Set<String>> numberMap = new HashMap<String, Set<String>>();
    List<Set<String>> numberList = new ArrayList<Set<String>>();
    Number(){
        zero.add("0");
        zero.add("영");
        numberList.add(zero);
        numberMap.put("0",zero);


        one.add("1");
        one.add("첫");
        one.add("일");
        numberList.add(one);
        numberMap.put("1",one);

        two.add("2");
        two.add("두");
        two.add("이");
        numberList.add(two);
        numberMap.put("2",two);

        three.add("3");
        three.add("세");
        three.add("삼");
        numberList.add(three);
        numberMap.put("3",three);

        four.add("4");
        four.add("네");
        four.add("사");
        numberList.add(four);
        numberMap.put("4",four);

        five.add("5");
        five.add("다섯");
        five.add("오");
        numberList.add(five);
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
    String findNumberByCnt(String answer, int cnt){
        for(int i=0;i<=cnt;i++){
            Set<String> number = numberList.get(i);
            Iterator<String> numberIterator = number.iterator();
            while(numberIterator.hasNext()){
                String numberStr = numberIterator.next();
                if (answer.contains(numberStr)){
                    return i+"";
                }
            }
        }
        return null;
    }
}
