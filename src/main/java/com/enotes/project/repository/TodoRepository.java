package com.enotes.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.project.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo,Integer>{

    List<Todo> findByCreatedBy(int i);

}
