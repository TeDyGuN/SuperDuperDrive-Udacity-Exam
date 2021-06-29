package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final UserService userService;
    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;

    public NoteController(UserService userService, NoteService noteService, FileService fileService, CredentialService credentialService) {
        this.userService = userService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    @PostMapping()
    public String postNote(Authentication authentication, Note note, Model model) {
        User user = this.userService.getUser(authentication.getName());
        String message = "Note updated correctly!";
        if(note.getNoteDescription().length() > 1000) {
            model.addAttribute("errorMessage", true);
            message = "The description field has a maximum limit of 1000 characters.";
        } else if( noteService.getNotesByTitleAndDescription(note.getNoteTitle(), note.getNoteDescription()).size() > 0 ) {
            model.addAttribute("errorMessage", true);
            message = "Duplicate note are not allowed";
        } else{
            if (note.getNoteId() == null ) {
                message = "Note created correctly!";
                note.setUserId(user.getUserId());
            }
            model.addAttribute("successMessage", true);
            this.noteService.createAndUpdateNote(note);
        }
        model.addAttribute("messageText", message);
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getFilesByUserId(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(user.getUserId()));
        return "home";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(Authentication authentication, @PathVariable(value = "id") Integer id, Model model) {
        User user = this.userService.getUser(authentication.getName());
        noteService.delete(id);
        model.addAttribute("successMessage", true);
        model.addAttribute("messageText", "Note deleted correctly!");
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getFilesByUserId(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(user.getUserId()));
        return "home";
    }
}
