package com.enotes.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.project.entity.FavouriteNotes;

public interface FavouriteNoteRepository extends JpaRepository<FavouriteNotes,Integer>{

    List<FavouriteNotes> findByUserId(Integer userId);

}
