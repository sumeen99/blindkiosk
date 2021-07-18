package com.abc.blindkiosk;

import java.util.ArrayList;

public class CartList {

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> sizeList = new ArrayList<>();
    ArrayList<String> tempList = new ArrayList<>();
    ArrayList customList = new ArrayList<>();
    ArrayList<Integer> priceList = new ArrayList<>();
    ArrayList<Integer> quantityList = new ArrayList<>();

    ArrayList menuList(){
        ArrayList arrayList = new ArrayList();
        arrayList.add(nameList);
        arrayList.add(sizeList);
        arrayList.add(tempList);
        arrayList.add(customList);
        arrayList.add(priceList);

        return arrayList;
    }

    void addName(String name){
        nameList.add(name);
    }

    void addSize(String size){
        sizeList.add(size);
    }

    void addTemp(String temp){
        tempList.add(temp);
    }

    void addCustom(ArrayList<String> custom){
        customList.add(custom);
    }

    void addPrice(Integer price){
        priceList.add(price);
    }

    void addQuantity(Integer quantity){
        quantityList.add(quantity);
    }

}