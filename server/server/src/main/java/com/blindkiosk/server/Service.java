package com.blindkiosk.server;



@org.springframework.stereotype.Service
public class Service {

    public StoreModel get(final String storeNum){
        return StoreModel.builder()
                .storeName("롯데리아")
                .build();
    }
}
