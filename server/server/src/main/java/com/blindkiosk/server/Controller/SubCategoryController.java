package com.blindkiosk.server.Controller;

import com.blindkiosk.server.Adapter;
import com.blindkiosk.server.Model.SubCategoryModel;
import com.blindkiosk.server.Response.SubCategoryResponse;
import com.blindkiosk.server.Service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SubCategoryController {

    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping("subcategory")
    @ResponseBody
    public SubCategoryResponse get(@RequestParam(value = "id") String id){

        List<String> errors=new ArrayList<>();
        List<SubCategoryModel> subCategoryModel = null;
        System.out.println(id);

        try{
            subCategoryModel= subCategoryService.getSubCategory(id);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.subCategoryResponse(subCategoryModel,errors);
    }

}
