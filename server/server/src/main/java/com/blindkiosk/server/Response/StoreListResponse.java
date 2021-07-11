package com.blindkiosk.server.Response;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import lombok.Builder;

import java.util.List;

public class StoreListResponse extends ApiResponse<List<String>> {

    @Builder
    public StoreListResponse(final List<String> storeModel, final List<String> errors){
        super(storeModel);
        this.setErrors(errors);
    }

}
