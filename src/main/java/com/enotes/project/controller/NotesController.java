package com.enotes.project.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.project.dto.NoteResponse;
import com.enotes.project.dto.NotesDto;
import com.enotes.project.entity.FileDetails;
import com.enotes.project.service.NotesService;
import com.enotes.project.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {
    @Autowired
    NotesService notesService;

    @PostMapping("/save")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception{
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

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws IOException{
        try{
        FileDetails fileDetails = notesService.getFileDetails(id);
        Path filePath = Paths.get(fileDetails.getPath());
        Resource resource = new InputStreamResource(notesService.downloadFile(filePath));
        String contentType = notesService.getContentType(filePath);
        HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDisposition(
                ContentDisposition.attachment()
                                  .filename(fileDetails.getOriginalFileName(), StandardCharsets.UTF_8)
                                  .build()
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } 
    }

    @GetMapping("/user-notes/")
    public  ResponseEntity<?> getAllNotesByUser(@RequestParam Integer id,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo ,
            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
                
        NoteResponse allNotes = notesService.getAllNotesByUser(id,pageNo,pageSize);
        // if(CollectionUtils.isEmpty(allNotes)){
        //     return CommonUtil.createErrorResponseMessage("not saved", HttpStatus.NO_CONTENT);
        // }
        return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Integer id){
        notesService.softDeleteNote(id);
        return CommonUtil.createBuildResponseMessage("note deleted", HttpStatus.OK);
    }
    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreNote(@PathVariable Integer id){
        notesService.restoreNote(id);
        return CommonUtil.createBuildResponseMessage("note deleted", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    public ResponseEntity<?> getUserRecycleBin(){
        Integer userId=1;
        List<NotesDto> userRecycleBin = notesService.getUserRecycleBin(userId);
        if(userRecycleBin.isEmpty()){
            return CommonUtil.createBuildResponseMessage("recycle bin empty", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(userRecycleBin, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> HardDeleteNote(@PathVariable Integer id){
        notesService.hardDeleteNote(id);
        return CommonUtil.createBuildResponseMessage("note deleted", HttpStatus.OK);
    }
    @DeleteMapping("/empty-bin")
    public ResponseEntity<?> emptyRecycleBin(){
        Integer notesDeleted=notesService.emptyRecycleBin();
        return CommonUtil.createBuildResponseMessage("note deleted-"+ notesDeleted, HttpStatus.OK);
    }
    
}
