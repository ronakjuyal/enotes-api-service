package com.enotes.project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.project.entity.Category;
import com.enotes.project.repository.CategoryRepository;
import com.enotes.project.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
    @Override
    public Boolean saveCategory(Category category){
        category.setIsDeleted(false);
        Category saveCategory= categoryRepo.save(category);
        if(ObjectUtils.isEmpty(saveCategory)){
            return false;
        }
        return true;
    }
    @Override
    public  List<Category> getAllCategory(){
        List<Category> catgories = categoryRepo.findAll();
        return catgories;
    }

}
