package com.blindkiosk.server.Repository;

import com.blindkiosk.server.Model.CustomModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRepository extends MongoRepository<CustomModel,String> {
    List<CustomModel> findBy_id(String id);
}
