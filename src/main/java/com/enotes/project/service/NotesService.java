package com.enotes.project.service;

import java.util.List;

import com.enotes.project.dto.NotesDto;

public interface NotesService {
    public Boolean saveNotes(NotesDto notesDto);
    public List<NotesDto> getAllNotes();
}
