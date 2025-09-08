package com.enotes.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.CollectionUtils;

import com.enotes.project.dto.CategoryDto;
import com.enotes.project.dto.CategoryResponse;
import com.enotes.project.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/save-category")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDta){
        Boolean saveCategory = categoryService.saveCategory(categoryDta);
        if(saveCategory){
            return new ResponseEntity<>("saved successfully",HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("not saved",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("hello")
    public String print(){
        return "hello"; 
    }
    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory(){
        List<CategoryResponse> allCategory = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(allCategory)){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(allCategory,HttpStatus.OK);
    }
    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategory(){
        List<CategoryResponse> activeCategory = categoryService.getActiveCategory();
        if(CollectionUtils.isEmpty(activeCategory)){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(activeCategory,HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryDetailById(@PathVariable Integer id) throws Exception{
        CategoryDto categoryDto=categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
        Boolean deleted = categoryService.deleteCategory(id);
        if(deleted){
            return new ResponseEntity<>("category delete success",HttpStatus.OK);
        }
        return new ResponseEntity<>("Category not deleted",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
