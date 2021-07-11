package com.blindkiosk.server.Controller;

import com.blindkiosk.server.Adapter;
import com.blindkiosk.server.Model.StoreModel;
import com.blindkiosk.server.Response.StoreResponse;
import com.blindkiosk.server.Response.StoreListResponse;
import com.blindkiosk.server.Service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StoreController {

    @Autowired
    private StoreService service;//이부분 다음에 this.service=service로 해야할것같음 지금은 넘어가자

    @GetMapping("store")
    @ResponseBody
    public StoreListResponse getStoreList(@RequestParam(value = "x") double x, @RequestParam(value = "y") double y){
        List<String> errors=new ArrayList<>();
        List<String> storeList= new ArrayList<>();

        try{
            storeList=service.putStoreList(x,y);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.storeListResponse(storeList,errors);
    }

    /*@RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Response getStoreMenu(@RequestParam(value = "store_name") String name){
        List<String> errors=new ArrayList<>();
        StoreModel storeModel = null;
        try{
            storeModel= service.get(id);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.storeResponse(storeModel,errors);
    }*/

    @GetMapping("menu")
    @ResponseBody
    public StoreResponse get(@RequestParam(value = "name") String name){
        List<String> errors=new ArrayList<>();
        StoreModel storeModel = null;
        System.out.println(name);
        try{
            storeModel= service.getMenu(name);
        }catch (final Exception e){
            errors.add(e.getMessage());
        }

        return Adapter.storeResponse(storeModel,errors);
    }


}
