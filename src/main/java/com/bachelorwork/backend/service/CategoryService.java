package com.bachelorwork.backend.service;

import com.bachelorwork.backend.model.Category;
import com.bachelorwork.backend.model.Item;
import com.bachelorwork.backend.repository.ICategoryRepository;
import com.bachelorwork.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    ICategoryRepository iCategoryRepository;

    @Autowired
    ItemRepository itemRepository;


    public Category findByCategoryName(String categoryName){
        return iCategoryRepository.findByName(categoryName).stream().findFirst().orElse(null);
    }

    public List<Category> getAll(){
        return (List<Category>) iCategoryRepository.findAll();
    }

    public String findByItem(Item item){
        if(itemRepository.findById(item.getIdItem()).isPresent())
            return iCategoryRepository.findByItemListContains(item).getName();
        return null;
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
