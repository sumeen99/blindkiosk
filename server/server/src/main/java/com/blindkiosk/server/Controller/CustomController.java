package com.blindkiosk.server.Controller;

import com.blindkiosk.server.Adapter;
import com.blindkiosk.server.Model.CustomModel;
import com.blindkiosk.server.Response.CustomResponse;
import com.blindkiosk.server.Service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomController {

    @Autowired
    CustomService customService;
    @GetMapping("custom")
    @ResponseBody
    public CustomResponse get(@RequestParam(value = "id") String id){

        List<String> errors=new ArrayList<>();
        List<CustomModel> customModels = null;

        try{
            customModels= customService.getCustom(id);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.customResponse(customModels,errors);
    }

}
