package com.enotes.project.service.impl;

import java.util.List;
import java.util.Optional;

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
        List<Category> categories = categoryRepo.findByIsDeletedFalse();
        List<CategoryResponse> categorylist = categories.stream().map(cat->mapper.map(cat,CategoryResponse.class)).toList();
        return categorylist;
    }
    @Override
    public List<CategoryResponse> getActiveCategory(){
        List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
        List<CategoryResponse> activeCategorylist = categories.stream().map(cat->mapper.map(cat,CategoryResponse.class)).toList();
        return activeCategorylist;
    }
    @Override
    public CategoryDto getCategoryById(Integer id){
        Optional<Category> findByCategory = categoryRepo.findByIdAndIsDeletedFalse(id);
        if(findByCategory.isPresent()){
            return mapper.map(findByCategory.get(), CategoryDto.class);
        }
        return null;
    }
    @Override
    public Boolean deleteCategory(Integer id){
        Optional<Category> categoryToDelete = categoryRepo.findById(id);
        if(categoryToDelete.isPresent()){
            categoryToDelete.get().setIsDeleted(true);
            categoryRepo.save(categoryToDelete.get());
            return true;
        }
        return false;
    }

}
