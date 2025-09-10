package com.enotes.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.project.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails,Integer>{

}
