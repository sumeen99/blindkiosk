package com.blindkiosk.server;


import com.blindkiosk.server.Repository.CustomRepository;
import com.blindkiosk.server.Repository.FoodRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    CustomRepository repository;

   /* @Test
    public void read(){
        System.out.println(repository.findBy_id("60eec497657b01f91546e511"));
        System.out.println("hihihihi!!");
    }*/


}
