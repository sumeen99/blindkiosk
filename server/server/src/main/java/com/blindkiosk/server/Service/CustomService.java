package com.blindkiosk.server.Service;

import com.blindkiosk.server.Model.CustomModel;
import com.blindkiosk.server.Repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomService {

    @Autowired
    CustomRepository customRepository;

    public CustomService(CustomRepository customRepository){
        this.customRepository=customRepository;
    }

    public List<CustomModel> getCustom(String id){
        return customRepository.findBy_id(id);
    }
}
