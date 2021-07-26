package com.blindkiosk.server.Response;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import com.blindkiosk.server.Model.RecommendModel;
import lombok.Builder;

import java.util.List;

public class RecommendResponse extends ApiResponse<List<RecommendModel>> {

    @Builder
    public RecommendResponse(final List<RecommendModel> recommendModels,final List<String> errors){
        super(recommendModels);
        this.setErrors(errors);
    }
}
