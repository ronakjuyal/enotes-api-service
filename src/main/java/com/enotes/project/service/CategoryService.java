package com.enotes.project.service;

import java.util.List;

import com.enotes.project.dto.CategoryDto;
import com.enotes.project.dto.CategoryResponse;

public interface CategoryService {

    public CategoryDto getCategoryById(Integer id) throws Exception;
    public Boolean saveCategory(CategoryDto categoryDto);
    public List<CategoryResponse> getAllCategory();
    public List<CategoryResponse> getActiveCategory();
    public Boolean deleteCategory(Integer id);
    

} 
