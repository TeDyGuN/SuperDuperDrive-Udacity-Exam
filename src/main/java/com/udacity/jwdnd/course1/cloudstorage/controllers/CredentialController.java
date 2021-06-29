package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
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
@RequestMapping("/credential")
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;
    private final NoteService noteService;
    private final FileService fileService;

    public CredentialController(UserService userService, CredentialService credentialService, NoteService noteService, FileService fileService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
    }

    @PostMapping()
    public String postCredential(Authentication authentication, Credential credential, Model model) {
        User user = this.userService.getUser(authentication.getName());
        String message = "Credential updated correctly!";
        if (credential.getCredentialId() == null ) {
            message = "Credential created correctly!";
            credential.setUserId(user.getUserId());
        }
        this.credentialService.createAndUpdateCredential(credential);
        model.addAttribute("successMessage", true);
        model.addAttribute("messageText", message);
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getFilesByUserId(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(user.getUserId()));
        return "home";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(Authentication authentication, @PathVariable(value = "id") Integer id, Model model) {
        User user = this.userService.getUser(authentication.getName());
        credentialService.delete(id);
        model.addAttribute("successMessage", true);
        model.addAttribute("messageText", "Credential deleted correctly!");
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getFilesByUserId(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(user.getUserId()));
        return "home";
    }
}
