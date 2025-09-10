package com.enotes.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.project.dto.NotesDto;
import com.enotes.project.service.NotesService;
import com.enotes.project.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {
    @Autowired
    NotesService notesService;

    @PostMapping("/save")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam MultipartFile file){
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if(saveNotes){
            return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/")
    public  ResponseEntity<?> getAllNotes(){
        List<NotesDto> allNotes = notesService.getAllNotes();
        if(CollectionUtils.isEmpty(allNotes)){
            return CommonUtil.createErrorResponseMessage("not saved", HttpStatus.NO_CONTENT);
        }
        return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
    }
}
