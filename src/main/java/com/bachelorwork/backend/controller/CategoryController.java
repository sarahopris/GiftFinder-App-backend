package com.bachelorwork.backend.controller;

import com.bachelorwork.backend.model.Category;
import com.bachelorwork.backend.model.Item;
import com.bachelorwork.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping(value ="/addCategories", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategory(@RequestBody List<Category> categoryList){
        return categoryService.addCategory(categoryList);
    }

    @GetMapping(value ="/getCategoryByItem", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCategoryBy(@RequestBody Item item){
        return categoryService.findByItem(item);
    }

    @GetMapping(value ="/getAllCategories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getAll(){
        return  categoryService.getAll();
    }


}
