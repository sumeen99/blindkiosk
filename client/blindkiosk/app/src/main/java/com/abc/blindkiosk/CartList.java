package com.abc.blindkiosk;

import java.io.Serializable;
import java.util.ArrayList;

public class CartList implements Serializable {

    String id;
    String name;
    String size;
    String temp;
    ArrayList<String> customList;
    Integer price;
    Integer quantity;


    public CartList(String id, String name, String size, String temp, ArrayList<String> custom, Integer price, Integer quantity){
        this.id = id;
        this.name = name;
        this.size = size;
        this.temp = temp;
        this.customList = custom;
        this.price = price;
        this.quantity = quantity;
    }


}
