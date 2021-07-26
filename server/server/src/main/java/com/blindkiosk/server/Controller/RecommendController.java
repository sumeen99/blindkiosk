package com.blindkiosk.server.Controller;

import com.blindkiosk.server.Adapter;
import com.blindkiosk.server.Model.RecommendModel;
import com.blindkiosk.server.Response.RecommendResponse;
import com.blindkiosk.server.Service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendController {

    @Autowired
    RecommendService recommendService;

    @GetMapping("recommend")
    @ResponseBody
    public RecommendResponse get(@RequestParam(value = "id") String id){

        List<String> errors=new ArrayList<>();
        List<RecommendModel> recommendModels = null;

        try{
            recommendModels= recommendService.getRecommend(id);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.recommendResponse(recommendModels,errors);
    }
}
