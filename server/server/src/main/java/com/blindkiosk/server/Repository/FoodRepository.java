package com.blindkiosk.server.Repository;

import com.blindkiosk.server.Model.FoodModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends MongoRepository<FoodModel,String> {
    List<FoodModel> findBySubcategoryId(String id);
}
