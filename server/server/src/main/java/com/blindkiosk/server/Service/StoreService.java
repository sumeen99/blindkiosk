package com.blindkiosk.server.Service;


import com.blindkiosk.server.Model.*;
import com.blindkiosk.server.Repository.StoreRepository;
import com.blindkiosk.server.SearchApi;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class StoreService {

    @Autowired
    private final StoreRepository storeRepository;
    public StoreService(StoreRepository storeRepository){this.storeRepository=storeRepository;}

    public StoreModel getMenu(String storeName){
        return storeRepository.findByName(storeName);
    }



    public List<String> putStoreList(double x,double y) throws ParseException {
        List<String> userStoreList; //사용자 주변 가게목록
        List<StoreModel> storeList;//DB에 있는 가게목록
        List<String> name=new ArrayList<>();//DB에 있는 가게목록이름들


        SearchApi searchApi= new SearchApi();
        userStoreList=searchApi.search(x,y,15);//사용자 주변 가게 목록
        storeList=storeRepository.findAllBy();//DB에 있는 가게 목록

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
        return userStoreList;
    }
}
