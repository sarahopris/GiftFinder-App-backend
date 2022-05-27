package com.bachelorwork.backend.repository;

import com.bachelorwork.backend.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByName(String categoryName);
}
