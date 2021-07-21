package com.blindkiosk.server.Response;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import com.blindkiosk.server.Model.CustomModel;
import lombok.Builder;

import java.util.List;

public class CustomResponse extends ApiResponse<List<CustomModel>> {

    @Builder
    public CustomResponse(List<CustomModel> customModels,List<String> errors){
        super(customModels);
        this.setErrors(errors);
    }
}
