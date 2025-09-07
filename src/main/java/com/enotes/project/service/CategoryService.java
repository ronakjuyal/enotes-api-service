package com.enotes.project.service;

import java.util.List;
import com.enotes.project.entity.Category;;

public interface CategoryService {

    public Boolean saveCategory(Category category);
    public List<Category> getAllCategory();

} 
