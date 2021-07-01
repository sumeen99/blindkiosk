package com.blindkiosk.server;

import com.blindkiosk.server.Model.StoreModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/store")
public class Controller {

    @Autowired
    private Service service;//이부분 다음에 this.service=service로 해야할것같음 지금은 넘어가자

    @RequestMapping(method = RequestMethod.GET,value = "/{x}/{y}")
    @ResponseBody
    public Response get(@PathVariable(value = "x") double x,@PathVariable(value = "y") double y){
        List<String> errors=new ArrayList<>();
        List<String> storeList= new ArrayList<>();

        try{
            storeList=service.putStoreList(x,y);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return storeList;
    }
    /*
    *   @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    @ResponseBody
    public Response get(@PathVariable(value = "id") Long id){
        List<String> errors=new ArrayList<>();
        StoreModel storeModel = null;
        System.out.println(id);
        try{
            storeModel= service.get(id);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.storeResponse(storeModel,errors);
    }*/


}
