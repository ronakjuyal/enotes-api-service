package com.enotes.project.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.project.dto.NoteResponse;
import com.enotes.project.dto.NotesDto;
import com.enotes.project.entity.FileDetails;


public interface NotesService {
    public Boolean saveNotes(String  note, MultipartFile file) throws Exception;
    public List<NotesDto> getAllNotes();
    public FileDetails getFileDetails(Integer id);
    public InputStream downloadFile(Path filePath) throws IOException;
    String getContentType(Path filePath) throws IOException;
    public NoteResponse getAllNotesByUser(Integer userId,Integer pageNo, Integer pageSize);
}
