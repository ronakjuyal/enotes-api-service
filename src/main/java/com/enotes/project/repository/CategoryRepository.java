package com.enotes.project.repository;

import com.enotes.project.entity.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> findByIsActiveTrue();

}
