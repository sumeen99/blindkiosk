package com.blindkiosk.server.Repository;

import com.blindkiosk.server.Model.FoodModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FoodRepository extends MongoRepository<FoodModel,String> {
    List<FoodModel> findBySubcategoryId(String id);
}
