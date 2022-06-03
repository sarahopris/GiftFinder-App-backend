package com.bachelorwork.backend.repository;

import com.bachelorwork.backend.model.Category;
import com.bachelorwork.backend.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByName(String categoryName);
    Category findByItemListContains(Item item);

    @Query("SELECT NEW CategoryResponse(c.idCategory,c.name) FROM Category c")
    Iterable<Category> findAll();
}
