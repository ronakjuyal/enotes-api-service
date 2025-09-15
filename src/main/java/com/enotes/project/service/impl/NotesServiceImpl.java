package com.enotes.project.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.project.dto.FavouriteNotesDto;
import com.enotes.project.dto.NoteResponse;
import com.enotes.project.dto.NotesDto;
import com.enotes.project.entity.FavouriteNotes;
import com.enotes.project.entity.FileDetails;
import com.enotes.project.entity.Notes;
import com.enotes.project.exception.ResourceNotFoundException;
import com.enotes.project.repository.CategoryRepository;
import com.enotes.project.repository.FavouriteNoteRepository;
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

    @Autowired
    FavouriteNoteRepository favouriteNoteRepository;

   @Value("${file.upload.path}")
	private String uploadPath;

    @Override
    public Boolean saveNotes(String  notes, MultipartFile file) throws Exception{

        //validation notes
        ObjectMapper ob = new ObjectMapper();
		NotesDto noteDto = ob.readValue(notes, NotesDto.class);
        if(!ObjectUtils.isEmpty(noteDto.getId())){
            if(!notesRepository.existsById(noteDto.getId())) throw new ResourceNotFoundException("invalid note id");
        }
        noteDto.setIsDeleted(false);
        noteDto.setDeletedOn(null);
        Notes newNotes = mapper.map(noteDto, Notes.class);
        FileDetails fileDetails=saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)) newNotes.setFileDetails(fileDetails);
        else newNotes.setFileDetails(null);

        Notes save = notesRepository.save(newNotes);
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
                fileDetails.setUploadFileName(uploadFileName);
                fileDetails.setDisplayFileName(getDisplayFileName(originalFilename));
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

    @Override
    public InputStream downloadFile(Path filePath) throws IOException {
        return Files.newInputStream(filePath, StandardOpenOption.READ);
    }

    @Override
    public FileDetails getFileDetails(Integer id) {
          FileDetails fileDetails = fileRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("file is notavilable"));
          return fileDetails;
    }
    @Override
    public String getContentType(Path filePath) throws IOException {
        String contentType = Files.probeContentType(filePath);
        return (contentType != null) ? contentType : "application/octet-stream";
    }

    @Override
    public NoteResponse getAllNotesByUser(Integer userId,Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Notes> pages = notesRepository.findByCreatedByAndIsDeletedFalse(userId,pageable);
        List<NotesDto> notesDto = pages.get().map(m->mapper.map(m,NotesDto.class)).toList();
        NoteResponse notes=NoteResponse.builder()
            .notes(notesDto)
            .pageNo(pages.getNumber())
            .totalPages(pages.getTotalPages())
            .pagesize(pages.getSize())
            .totalElement(pages.getTotalElements())
            .isFirst(pages.isFirst())
            .isLast(pages.isLast())
            .build();
        return notes;
    }

    @Override
    public void softDeleteNote(Integer id) {
        Notes note = notesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("notes id invalid not found"));
        note.setIsDeleted(true);
        note.setDeletedOn(LocalDateTime.now());
        notesRepository.save(note);
    }

    @Override
    public void restoreNote(Integer id) {
        Notes note = notesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("notes id invalid not found"));
        note.setIsDeleted(false);
        note.setDeletedOn(null);
        notesRepository.save(note);
        
    }

    @Override
    public List<NotesDto> getUserRecycleBin(Integer userId) {
        List<Notes> notes=notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        List<NotesDto> notesDto = notes.stream().map(note->mapper.map(note, NotesDto.class)).toList();
        return notesDto;
        
    }

    @Override
    public void hardDeleteNote(Integer id) {
        Notes note = notesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("notes fond to delete"));
        if(note.getIsDeleted()){
            notesRepository.delete(note);
        } else{
            throw new IllegalArgumentException("cannot delete, first move to notes to recycle bin");
        }
    }

    @Override
    @Transactional
    public int emptyRecycleBin() {
        return notesRepository.deleteByIsDeletedTrue();
    }

    @Override
    public void favouriteNotes(Integer notesId) {
        Integer userId=1;
        Notes notes = notesRepository.findById(notesId)
                    .orElseThrow(()->new ResourceNotFoundException("notes not found - invalid notesId"));
        FavouriteNotes favouriteNotes= FavouriteNotes.builder()
                .notes(notes)
                .userId(userId)
                .build();
        favouriteNoteRepository.save(favouriteNotes);
    }

    @Override
    public List<FavouriteNotesDto> getUserFavouriteNotes() {
        Integer userId=1;
        List<FavouriteNotes> favNotes = favouriteNoteRepository.findByUserId(userId);
        List<FavouriteNotesDto> map = favNotes.stream().map(fav->mapper.map(fav, FavouriteNotesDto.class)).toList();
        return map;
    }

    @Override
    public void unFavouriteNotes(Integer favouriteNotesId) {
        
        if (!favouriteNoteRepository.existsById(favouriteNotesId)) {
            throw new ResourceNotFoundException("notes not found - invalid FavouriteNotesId");
        }   
        favouriteNoteRepository.deleteById(favouriteNotesId);
    }

    @Override
    public void copyNotes(Integer id) {
        Notes note = notesRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("notes id invalid - not found"));
        Notes copyNotes=Notes.builder()
                .title(note.getTitle())
                .description(note.getDescription())
                .category(note.getCategory())
                .isDeleted(false)
                .fileDetails(null)
                .build();
        if(ObjectUtils.isEmpty(notesRepository.save(copyNotes))){
            throw new ResourceNotFoundException("failed while coping notes - try again");
        }
    }
    
}   
