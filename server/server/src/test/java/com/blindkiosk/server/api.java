package com.blindkiosk.server;


import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

public class api {

    @Test
    public void putStoreList() throws ParseException {
        SearchApi searchApi= new SearchApi();
        System.out.println(searchApi.search(127.07353705226912,37.54717476516024,  15));
    }
}
