package com.blindkiosk.server.Service;

import com.blindkiosk.server.Model.FoodModel;
import com.blindkiosk.server.Repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository){
        this.foodRepository=foodRepository;
    }

    public List<FoodModel> getFood(String id){
        return foodRepository.findBySubcategoryId(id);
    }

    public List<FoodModel> getAllFood(String id){
        return foodRepository.findByStoreId(id);
    }


}
