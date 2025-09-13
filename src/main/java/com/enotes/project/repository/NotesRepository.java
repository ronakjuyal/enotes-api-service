package com.enotes.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.project.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes,Integer>{

    Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);

    List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);

    List<Notes> findByIsDeletedAndDeletedOnBefore(boolean isDeleted, LocalDateTime cutOffDate);

    int deleteByIsDeletedTrue();
    
    
} 
