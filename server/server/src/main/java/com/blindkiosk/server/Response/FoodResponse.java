package com.blindkiosk.server.Response;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import com.blindkiosk.server.Model.FoodModel;
import lombok.Builder;

import java.util.List;

public class FoodResponse extends ApiResponse<List<FoodModel>> {

    @Builder
    public FoodResponse(final List<FoodModel> foodModels,final List<String> errors){
        super(foodModels);
        this.setErrors(errors);

    }

}
