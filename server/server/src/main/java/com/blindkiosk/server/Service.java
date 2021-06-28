package com.blindkiosk.server;



@org.springframework.stereotype.Service
public class Service {

    public StoreModel get(final Long id){
        return StoreModel.builder()
                .id(id)
                .storeName("롯데리아")
                .build();
    }
}
