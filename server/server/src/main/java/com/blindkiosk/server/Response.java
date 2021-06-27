package com.blindkiosk.server;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import lombok.Builder;

import java.util.List;

public class Response extends ApiResponse<StoreModel> {

    @Builder
    public Response(final StoreModel storeModel, final List<String> errors){
        super(storeModel);
        this.setErrors(errors);
    }

}
