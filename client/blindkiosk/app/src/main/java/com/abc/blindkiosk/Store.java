package com.abc.blindkiosk;

import java.util.ArrayList;
import java.util.List;

public class Store {
    List<String> storeList = new ArrayList<String>();

    Store(){
        storeList.add("맥도날드");
        storeList.add("공차");
    }

    String findStore(String storeName){
        for(int i=0;i<storeList.size();i++){
            if(storeName.contains(storeList.get(i))){
                return storeList.get(i);
            }
        }
        return null;
    }
}
