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
import com.enotes.project.exception.ExistDataException;
import com.enotes.project.exception.ResourceNotFoundException;
import com.enotes.project.repository.CategoryRepository;
import com.enotes.project.service.CategoryService;
import com.enotes.project.util.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    Validation validation;
    @Override
    public Boolean saveCategory(CategoryDto categoryDto){
        validation.categoryValidation(categoryDto);
        Boolean exist= categoryRepo.existsByName(categoryDto.getName());
        if(exist){
            throw new ExistDataException("categry already exist");
        }
        Category category = mapper.map(categoryDto, Category.class);
        if(ObjectUtils.isEmpty(category.getId())){
            category.setIsDeleted(false);
        }else{
            updateCategory(category);
        }
        Category saveCategory= categoryRepo.save(category);
        if(ObjectUtils.isEmpty(saveCategory)){
            return false;
        }
        return true;
    }
    private void updateCategory(Category category) {
        Optional<Category> findById = categoryRepo.findById(category.getId());
        if(findById.isPresent()){
            Category exiCategory=findById.get();
            category.setCreatedBy(exiCategory.getCreatedBy());
            category.setCreatedOn(exiCategory.getCreatedOn());
            category.setIsDeleted(exiCategory.getIsDeleted());
        }
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
    public CategoryDto getCategoryById(Integer id) throws Exception{
        Category category = categoryRepo.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(()->new ResourceNotFoundException("category not found with id = "+id));
        return mapper.map(category, CategoryDto.class);
       
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
