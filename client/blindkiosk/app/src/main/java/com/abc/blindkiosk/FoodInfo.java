package com.abc.blindkiosk;

import java.util.List;

public class FoodInfo {
    String id;
    String name;
    String subcategoryId;
    List<String> customId;
    boolean temp;
    String size;
    String price;



    FoodInfo(String id, String name, String subcategoryId, List<String> customId, boolean temp, String size, String price){
        this.id = id;
        this.name = name;
        this.subcategoryId = subcategoryId;
        this.temp = temp;
        this.customId = customId;
        this.size = size;
        this.price = price;
    }


}
