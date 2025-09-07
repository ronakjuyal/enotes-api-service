package com.enotes.project.service;

import java.util.List;

import com.enotes.project.dto.CategoryDto;
import com.enotes.project.dto.CategoryResponse;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto categoryDto);
    public List<CategoryResponse> getAllCategory();
    public List<CategoryResponse> getActiveCategory();

} 
