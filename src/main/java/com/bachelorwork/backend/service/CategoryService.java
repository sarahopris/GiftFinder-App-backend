package com.bachelorwork.backend.service;

import com.bachelorwork.backend.model.Category;
import com.bachelorwork.backend.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    ICategoryRepository iCategoryRepository;


    public Category findByCategoryName(String categoryName){
        return iCategoryRepository.findByName(categoryName).stream().findFirst().orElse(null);
    }

    @Transactional
    public ResponseEntity<?> addCategory(List<Category> categoryList){
        for(Category category: categoryList) {
            Category categoryToSave = new Category();
            if (findByCategoryName(category.getName()) != null)
                return new ResponseEntity<>("already existent", HttpStatus.BAD_REQUEST);
            categoryToSave.setName(category.getName());
            iCategoryRepository.save(categoryToSave);

        }
        return new ResponseEntity<>("categories added", HttpStatus.OK);
    }




}
