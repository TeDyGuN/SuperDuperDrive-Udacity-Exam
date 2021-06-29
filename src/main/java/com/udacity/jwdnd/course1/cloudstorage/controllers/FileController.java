package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/file")
public class FileController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final NoteService noteService;
    private final FileService fileService;

    public FileController(UserService userService, CredentialService credentialService, NoteService noteService, FileService fileService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
    }

    @PostMapping()
    public String postCredential(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        User user = this.userService.getUser(authentication.getName());
        String message = "";
        if( fileUpload.isEmpty()) {
            model.addAttribute("errorMessage", true);
            message = "There is an empty file!";
        } else if ( fileService.getFileByFilenameAndUsername(fileUpload.getOriginalFilename(), user.getUserId()).size() > 0 ) {
            model.addAttribute("errorMessage", true);
            message = "There is a file with the same name!";
        } else if ( fileUpload.getSize() * 0.00000095367432 > 5 ) {
            model.addAttribute("errorMessage", true);
            message = "The maximum file size allowed is 5MB";
        } else {
            fileService.createFile(fileUpload, user.getUserId());
            model.addAttribute("successMessage", true);
            message = "File uploaded correctly!";
        }
        model.addAttribute("messageText", message);
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(user.getUserId()));
        model.addAttribute("files", this.fileService.getFilesByUserId(user.getUserId()));
        return "home";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(Authentication authentication, @PathVariable(value = "id") Integer id, Model model) {
        User user = this.userService.getUser(authentication.getName());
        fileService.delete(id);
        model.addAttribute("successMessage", true);
        model.addAttribute("messageText", "File deleted correctly!");
        model.addAttribute("notes", this.noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getCredentialsByUserId(user.getUserId()));
        model.addAttribute("files", this.fileService.getFilesByUserId(user.getUserId()));
        return "home";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("id") Integer id) {

        File file = this.fileService.getFileById(id);

        byte[] fileData = file.getFileData();

        InputStream inputStream = new ByteArrayInputStream(fileData);

        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getFileName())
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }
}
