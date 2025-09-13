package com.enotes.project.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enotes.project.entity.Notes;
import com.enotes.project.repository.NotesRepository;

@Component  
public class NoteScheduler {
    @Autowired
    NotesRepository notesRepository;
    @Scheduled(cron = "0 0 0 * * *")
    void deleteNotesScheduler(){
        LocalDateTime coutOfDate = LocalDateTime.now().minusDays(7);
        List<Notes> DeleteNotes = notesRepository.findByIsDeletedAndDeletedOnBefore(true,coutOfDate);
        notesRepository.deleteAll(DeleteNotes);
    }

}
