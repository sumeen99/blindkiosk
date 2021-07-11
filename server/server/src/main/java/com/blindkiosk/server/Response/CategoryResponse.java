package com.blindkiosk.server.Response;

import com.blindkiosk.server.ApiResponse.ApiResponse;
import com.blindkiosk.server.Model.CategoryModel;
import lombok.Builder;

import java.util.List;

public class CategoryResponse extends ApiResponse<List<CategoryModel>> {
    @Builder
    public CategoryResponse(final List<CategoryModel> categoryModel, final List<String> errors){
        super(categoryModel);
        this.setErrors(errors);
    }
}
