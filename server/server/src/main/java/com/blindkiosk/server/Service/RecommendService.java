package com.blindkiosk.server.Service;

import com.blindkiosk.server.Model.RecommendModel;
import com.blindkiosk.server.Repository.RecommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendService {

    @Autowired
    private final RecommendRepository recommendRepository;

    public RecommendService(RecommendRepository recommendRepository){
        this.recommendRepository=recommendRepository;
    }

    public List<RecommendModel> getRecommend(String id){
        return recommendRepository.findByMenuId(id);
    }

}
