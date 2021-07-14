package com.blindkiosk.server;

import com.blindkiosk.server.Model.*;
import com.blindkiosk.server.Response.*;

import java.util.List;

public class Adapter {

    public static StoreResponse storeResponse(final StoreModel storeModel, final List<String> errors){
        return StoreResponse.builder().storeModel(storeModel).errors(errors).build();
    }

    public static StoreListResponse storeListResponse(final List<String> storeModel, final List<String> errors){
        return StoreListResponse.builder().storeModel(storeModel).errors(errors).build();
    }

    public static CategoryResponse categoryResponse(final List<CategoryModel> categoryModel, final List<String> errors){
        return CategoryResponse.builder().categoryModel(categoryModel).errors(errors).build();
    }

    public static SubCategoryResponse subCategoryResponse(final List<SubCategoryModel> subCategoryModel, final List<String> errors){
        return SubCategoryResponse.builder().subCategoryModel(subCategoryModel).errors(errors).build();
    }

    public static FoodResponse foodResponse(final List<FoodModel> foodModels, final List<String> errors){
        return FoodResponse.builder().foodModels(foodModels).errors(errors).build();
    }

    public static CustomResponse customResponse(final List<CustomModel> customModels, final List<String> errors){
        return CustomResponse.builder().customModels(customModels).errors(errors).build();
    }
}
