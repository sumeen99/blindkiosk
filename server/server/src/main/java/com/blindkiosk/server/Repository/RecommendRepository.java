package com.blindkiosk.server.Repository;

import com.blindkiosk.server.Model.RecommendModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendRepository extends MongoRepository<RecommendModel,String> {
List<RecommendModel> findByMenuId(String id);
}
