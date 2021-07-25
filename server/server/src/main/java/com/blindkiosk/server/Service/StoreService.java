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





    /*public StoreModel get(final Long id){
        MenuModel menuModel= new MenuModel();
        CategoryModel categoryModel = new CategoryModel();
        SubCategoryModel subCategoryModel= new SubCategoryModel();
        FoodModel foodModel=new FoodModel();

        List<String> material=new ArrayList<>();
        material.add("햄");
        material.add("치즈");
        material.add("쇠고기");
        material.add("양상추");
        material.add("피클");
        material.add("양파");
        foodModel.setMaterial(material);
        //음식에 대한 재료
        Map<String,FoodModel> foodList=new HashMap<>();
        foodList.put("빅맥",foodModel);
        foodList.put("슈니언 버거",null);
        subCategoryModel.setFood(foodList);
        //음식 넣기
        Map<String,SubCategoryModel> subCategoryList= new HashMap<>();
        subCategoryList.put("세트메뉴",null);
        subCategoryList.put("단품메뉴",subCategoryModel);
        categoryModel.setSubCategory(subCategoryList);
        //서브카테고리 넣기
        Map<String,CategoryModel> categoryList= new HashMap<>();
        categoryList.put("버거",categoryModel);
        categoryList.put("맥런치",null);
        menuModel.setCategory(categoryList);
        //카테고리넣기



        return StoreModel.builder()
                .id(id)
                .storeName("맥도날드")
                .menu(menuModel)
                .build();
    }*/

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
