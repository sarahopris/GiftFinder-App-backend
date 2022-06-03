package com.bachelorwork.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CategoryResponse {
    private String category;
    private Long id;

    public CategoryResponse(Long id,String category) {
        this.category = category;
        this.id = id;
    }

    public CategoryResponse(){}

    public String getCategoryName() {
        return category;
    }

    public void setCategoryName(String category) {
        this.category = category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
