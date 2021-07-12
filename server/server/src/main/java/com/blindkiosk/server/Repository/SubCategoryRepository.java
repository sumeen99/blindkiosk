package com.blindkiosk.server.Repository;

import com.blindkiosk.server.Model.SubCategoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends MongoRepository<SubCategoryModel,String> {
    List<SubCategoryModel> findByCategoryId(String id);
}
