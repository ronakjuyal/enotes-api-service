package com.enotes.project.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.project.dto.NotesDto;
import com.enotes.project.entity.FileDetails;
import com.enotes.project.entity.Notes;
import com.enotes.project.exception.ResourceNotFoundException;
import com.enotes.project.repository.CategoryRepository;
import com.enotes.project.repository.FileRepository;
import com.enotes.project.repository.NotesRepository;
import com.enotes.project.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class NotesServiceImpl implements NotesService{
    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    FileRepository fileRepository;

   @Value("${file.upload.path}")
	private String uploadPath;

    @Override
    public Boolean saveNotes(String  notes, MultipartFile file) throws Exception{

        //validation notes
        ObjectMapper ob = new ObjectMapper();
		NotesDto noteDto = ob.readValue(notes, NotesDto.class);

        boolean exists = categoryRepository.existsById(noteDto.getCategory().getId());
        if(!exists){
            throw new ResourceNotFoundException("category is invalid");
        }
        Notes Notes = mapper.map(noteDto, Notes.class);
        FileDetails fileDetails=saveFileDetails(file);
        Notes save = notesRepository.save(Notes);
        if(ObjectUtils.isEmpty(save)){
            return false;
        }
        return true;
    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException{
        if(!ObjectUtils.isEmpty(file) && !file.isEmpty()){
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            List<String> extentionAllow = Arrays.asList("pdf","xlsx","png","jpg");
            if(!extentionAllow.contains(extension)){
                throw new IllegalArgumentException("Invalid file format, ulpoad only .pdf .xlsx .png .jpg");
            }
            UUID randomUUID = UUID.randomUUID();
            String uploadFileName=randomUUID+"."+extension;
            File saveFile = new File(uploadPath);
            if(!saveFile.exists()){
                saveFile.mkdir();
            }
            String storePath=uploadPath.concat(uploadFileName);

            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if(upload!=0){
                FileDetails fileDetails=new FileDetails();
                fileDetails.setOriginalFileName(originalFilename);
                fileDetails.setUploadFileName(getDisplayFileName(uploadFileName));
                fileDetails.setDisplayFileName(storePath);
                fileDetails.setFileSize(file.getSize());
                fileDetails.setPath(storePath);
                FileDetails savDetails=fileRepository.save(fileDetails);
                return savDetails;
                
            }
        }
        return null;
    }

    private String getDisplayFileName(String originalFilename) {
	
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);

		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + "." + extension;
		return fileName;
	}

    @Override
    public List<NotesDto> getAllNotes() {
        List<NotesDto> notesDto = notesRepository.findAll().stream().map(note->mapper.map(note, NotesDto.class)).toList();
        return notesDto;
    }

}
