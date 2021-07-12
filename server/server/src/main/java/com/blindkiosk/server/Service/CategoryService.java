package com.blindkiosk.server.Service;


import com.blindkiosk.server.Model.CategoryModel;
import com.blindkiosk.server.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){this.categoryRepository=categoryRepository;}

    public List<CategoryModel> getCategory(String id){
        return categoryRepository.findByStoreId(id);
    }
}
