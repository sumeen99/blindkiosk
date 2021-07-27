package com.blindkiosk.server.Service;

import com.blindkiosk.server.Model.RecommendModel;
import com.blindkiosk.server.Repository.RecommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendService {

    @Autowired
    private final RecommendRepository recommendRepository;

    public RecommendService(RecommendRepository recommendRepository){
        this.recommendRepository=recommendRepository;
    }

    public RecommendModel getRecommend(List<String> idList){
        List<RecommendModel> recommendList=new ArrayList<>();
        for(String id:idList){
            RecommendModel recommend=recommendRepository.findByMenuId(id);
            recommendList.add(recommend);
        }
        int cnt=0;
        double max=0;
        int i=0;
        while(recommendList.size()>cnt){
            RecommendModel recommendModel=recommendList.get(cnt);
            if(cnt==0){max=Double.parseDouble(recommendModel.getPercent());}
            if(max<Double.parseDouble(recommendModel.getPercent())){
                max=Double.parseDouble(recommendModel.getPercent());
                i=cnt;
            }
            cnt++;
        }
        return recommendList.get(i);
    }

}
