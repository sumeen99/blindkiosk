package com.abc.blindkiosk;

import java.util.List;

public class FoodInfo {
    String id;
    String name;
    String subcategoryId;
    List<String> customId;
    boolean temp;
    List<String> size;
    List<String> price;



    FoodInfo(String id, String name, String subcategoryId, List<String> customId, boolean temp, List<String> size, List<String> price){
        this.id = id;
        this.name = name;
        this.subcategoryId = subcategoryId;
        this.temp = temp;
        this.customId = customId;
        this.size = size;
        this.price = price;
    }


}
