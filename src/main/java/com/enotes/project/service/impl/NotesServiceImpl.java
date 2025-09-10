package com.enotes.project.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.project.dto.NotesDto;
import com.enotes.project.entity.Notes;
import com.enotes.project.exception.ResourceNotFoundException;
import com.enotes.project.repository.CategoryRepository;
import com.enotes.project.repository.NotesRepository;
import com.enotes.project.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService{
    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Boolean saveNotes(NotesDto notesDto) {
        //validation notes
        boolean exists = categoryRepository.existsById(notesDto.getCategory().getId());
        if(!exists){
            throw new ResourceNotFoundException("category is invalid");
        }
        Notes Notes = mapper.map(notesDto, Notes.class);
        Notes save = notesRepository.save(Notes);
        if(ObjectUtils.isEmpty(save)){
            return false;
        }
        return true;
    }

    @Override
    public List<NotesDto> getAllNotes() {
        List<NotesDto> notesDto = notesRepository.findAll().stream().map(note->mapper.map(note, NotesDto.class)).toList();
        return notesDto;
    }

}
