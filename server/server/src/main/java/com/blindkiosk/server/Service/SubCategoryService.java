package com.blindkiosk.server.Service;

import com.blindkiosk.server.Model.SubCategoryModel;
import com.blindkiosk.server.Repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {

    @Autowired
    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryService(SubCategoryRepository subCategoryRepository){
        this.subCategoryRepository=subCategoryRepository;
    }

    public List<SubCategoryModel> getSubCategory(String id){
        return subCategoryRepository.findByCategoryId(id);
    }


}
