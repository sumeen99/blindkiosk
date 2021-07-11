package com.blindkiosk.server.Controller;

import com.blindkiosk.server.Adapter;
import com.blindkiosk.server.Model.FoodModel;
import com.blindkiosk.server.Response.FoodResponse;
import com.blindkiosk.server.Service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FoodController {

    @Autowired
    FoodService foodService;

    @GetMapping("food")
    @ResponseBody
    public FoodResponse get(@RequestParam(value = "id") String id){

        List<String> errors=new ArrayList<>();
        List<FoodModel> foodModels = null;
        System.out.println(id);

        try{
            foodModels= foodService.getFood(id);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.foodResponse(foodModels,errors);
    }
}
