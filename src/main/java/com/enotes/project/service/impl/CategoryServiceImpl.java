package com.enotes.project.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.project.dto.CategoryDto;
import com.enotes.project.dto.CategoryResponse;
import com.enotes.project.entity.Category;
import com.enotes.project.repository.CategoryRepository;
import com.enotes.project.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ModelMapper mapper;
    @Override
    public Boolean saveCategory(CategoryDto categoryDto){
        Category category = mapper.map(categoryDto, Category.class);
        category.setIsDeleted(false);
        Category saveCategory= categoryRepo.save(category);
        if(ObjectUtils.isEmpty(saveCategory)){
            return false;
        }
        return true;
    }
    @Override
    public  List<CategoryResponse> getAllCategory(){
        List<Category> categories = categoryRepo.findAll();
        List<CategoryResponse> categorylist = categories.stream().map(cat->mapper.map(cat,CategoryResponse.class)).toList();
        return categorylist;
    }
    @Override
    public List<CategoryResponse> getActiveCategory(){
        List<Category> categories = categoryRepo.findByIsActiveTrue();
        List<CategoryResponse> activeCategorylist = categories.stream().map(cat->mapper.map(cat,CategoryResponse.class)).toList();
        return activeCategorylist;
    }

}
