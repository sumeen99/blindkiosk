package com.blindkiosk.server;


import com.blindkiosk.server.Model.StoreModel;
import com.blindkiosk.server.Repository.CategoryRepository;
import com.blindkiosk.server.Repository.FoodRepository;
import com.blindkiosk.server.Repository.SubCategoryRepository;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class api {

   /* @Test
    public void putStoreList() throws ParseException {
        SearchApi searchApi= new SearchApi();
        System.out.println(searchApi.search(127.07353705226912,37.54717476516024,  15));
    }*/

    @Autowired
    FoodRepository repository;

    @Test
    public void read(){
        System.out.println(repository.findBySubcategoryId("60e8964c81a13384e4109fcc"));
        System.out.println("hihihihi!!");
    }


}
