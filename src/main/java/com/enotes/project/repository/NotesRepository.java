package com.enotes.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.project.entity.Notes;

public interface NotesRepository extends JpaRepository<Notes,Integer>{
    
    
} 
