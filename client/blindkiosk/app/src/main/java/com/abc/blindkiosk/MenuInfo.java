package com.abc.blindkiosk;

public class MenuInfo {
    String id;
    String parentCategoryId;
    String name;

    MenuInfo(String id, String parentCategoryId, String name){
        this.id = id;
        this.parentCategoryId = parentCategoryId;
        this.name = name;
    }
}
