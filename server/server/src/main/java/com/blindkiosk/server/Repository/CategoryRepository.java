package com.blindkiosk.server.Repository;

import com.blindkiosk.server.Model.CategoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryModel,String> {
    public List<CategoryModel> findByStoreId(String storeId);
}
