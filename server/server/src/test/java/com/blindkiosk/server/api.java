package com.blindkiosk.server;


import com.blindkiosk.server.Model.StoreModel;
import com.blindkiosk.server.Repository.CustomRepository;
import com.blindkiosk.server.Repository.FoodRepository;
import com.blindkiosk.server.Repository.StoreRepository;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class api {
    @Autowired
    StoreRepository repository;

   /* @Test
    public void putStoreList() throws ParseException {
        List<String> userStoreList; //사용자 주변 가게목록
        List<StoreModel> storeList;//DB에 있는 가게목록
        List<String> name=new ArrayList<>();//DB에 있는 가게목록이름들


        SearchApi searchApi= new SearchApi();
        userStoreList=searchApi.search(127.15517476659225,37.55277743405474,15);//사용자 주변 가게 목록
        storeList=repository.findAllBy();//DB에 있는 가게 목록

        for(StoreModel list:storeList){
            name.add(list.getName());
        }

        Iterator<String> list=userStoreList.iterator();
        while(list.hasNext()){
            String a=list.next();
            System.out.println(a);
            int i=1;
            for(String store:name){
                if(a.contains(store))break;
                if(!a.contains(store)&&i==name.size()){
                    list.remove();
                    break;
                }
                i++;
            }
        }

    }*/





   /* @Test
    public void read(){
        System.out.println(repository.findBy_id("60eec497657b01f91546e511"));
        System.out.println("hihihihi!!");
    }*/


}
