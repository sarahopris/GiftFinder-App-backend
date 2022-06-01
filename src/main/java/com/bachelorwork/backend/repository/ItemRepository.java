package com.bachelorwork.backend.repository;

import com.bachelorwork.backend.model.Category;
import com.bachelorwork.backend.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

        Optional<Item> findByItemName(String itemName);
        List<Item> findByCategory(Category category);
}
