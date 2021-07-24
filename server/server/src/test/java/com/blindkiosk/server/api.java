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

    @Test
    public void putStoreList() throws ParseException {
        List<String> userStoreList;
        List<StoreModel> storeList;
        List<String> name=new ArrayList<>();
        List<String> tmpList=new ArrayList<>();
        int userStoreIndex=0;

        SearchApi searchApi= new SearchApi();
        userStoreList=searchApi.search(127.15517476659225,37.55277743405474,15);//사용자 주변 가게 목록
        tmpList=userStoreList;
        storeList=repository.findAllBy();//DB에 있는 가게 목록

        //userStoreIndex=userStoreList.size();
        System.out.println(userStoreList);
        System.out.println(storeList);
        System.out.println(tmpList);
        for(StoreModel list:storeList){
            name.add(list.getName());
        }
        Iterator<String> list=userStoreList.iterator();
        System.out.println("^^"+list);
        while(list.hasNext()){
            //System.out.println(list.next()+"1");
            if(!name.contains(list.next())){
              //  System.out.println(list.next()+"2");
                list.remove(); //indexOf 함수쓰는건 시간복잡도를 갖게되므로 그냥 i 쓰는게 나을듯

            }
            //System.out.println(list.next()+"3");
            userStoreIndex++;
        }


        System.out.println(userStoreList);
        System.out.println(tmpList);
    }



   /* @Test
    public void read(){
        System.out.println(repository.findBy_id("60eec497657b01f91546e511"));
        System.out.println("hihihihi!!");
    }*/


}
