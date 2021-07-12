package com.blindkiosk.server.Controller;


import com.blindkiosk.server.Adapter;
import com.blindkiosk.server.Model.CategoryModel;
import com.blindkiosk.server.Response.CategoryResponse;
import com.blindkiosk.server.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("category")
    @ResponseBody
    public CategoryResponse get(@RequestParam(value = "id") String id){

        List<String> errors=new ArrayList<>();
        List<CategoryModel> categoryModel = null;
        System.out.println(id);

        try{
            categoryModel= categoryService.getCategory(id);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.categoryResponse(categoryModel,errors);
    }
}
