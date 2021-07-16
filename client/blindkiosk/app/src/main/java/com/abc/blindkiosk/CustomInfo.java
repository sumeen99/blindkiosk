package com.abc.blindkiosk;

import java.util.List;

public class CustomInfo {
    String id;
    String name;
    List<String> type;
    List<String> price;

    public CustomInfo(String id, String name, List<String> type, List<String> price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }
}
