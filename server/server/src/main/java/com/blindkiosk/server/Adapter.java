package com.blindkiosk.server;

import java.util.List;

public class Adapter {

    public static Response storeResponse(final StoreModel storeModel, final List<String> errors){
        return Response.builder().storeModel(storeModel).errors(errors).build();
    }
}
