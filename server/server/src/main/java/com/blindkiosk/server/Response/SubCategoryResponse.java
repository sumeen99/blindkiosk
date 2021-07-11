package com.blindkiosk.server.Response;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import com.blindkiosk.server.Model.SubCategoryModel;
import lombok.Builder;

import java.util.List;

public class SubCategoryResponse extends ApiResponse<List<SubCategoryModel>> {

    @Builder
    public SubCategoryResponse(final List<SubCategoryModel> subCategoryModel, final List<String> errors){
        super(subCategoryModel);
        this.setErrors(errors);
    }
}
