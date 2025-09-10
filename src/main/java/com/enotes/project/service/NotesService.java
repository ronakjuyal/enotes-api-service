package com.enotes.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.project.dto.NotesDto;

public interface NotesService {
    public Boolean saveNotes(String  note, MultipartFile file) throws Exception;
    public List<NotesDto> getAllNotes();
}
