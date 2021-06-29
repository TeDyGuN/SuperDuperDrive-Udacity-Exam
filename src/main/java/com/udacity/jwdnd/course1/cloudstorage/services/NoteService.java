package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    @PostConstruct
    public void PostConstruct() {
        System.out.println("Creating NoteService bean");
    }

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void createAndUpdateNote(Note note) {
        if ( note.getNoteId() != null ) {
            this.noteMapper.update(note);
        } else {
            this.noteMapper.insert(note);
        }
    }

    public List<Note> getNotes(Integer id) {
        return this.noteMapper.getNotesByUserId(id);
    }

    public void delete(Integer id) {
        this.noteMapper.delete(id);
    }
}
