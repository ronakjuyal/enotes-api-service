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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
}
