package com.blindkiosk.server.Response;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import com.blindkiosk.server.Model.StoreModel;
import lombok.Builder;


import java.util.List;

public class StoreResponse extends ApiResponse<StoreModel> {

    @Builder
    public StoreResponse(final StoreModel storeModel, final List<String> errors){
        super(storeModel);
        this.setErrors(errors);
    }
}
