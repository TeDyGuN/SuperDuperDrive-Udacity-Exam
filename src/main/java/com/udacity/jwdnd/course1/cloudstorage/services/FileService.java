package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    @PostConstruct
    public void PostConstruct() {
        System.out.println("Creating FileService bean");
    }

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void createFile(MultipartFile multipartFile, Integer userId) throws IOException {
        byte[] fileByte = multipartFile.getBytes();
        File fileUploaded = new File(null, multipartFile.getOriginalFilename(), String.valueOf(multipartFile.getSize()), multipartFile.getContentType(), userId, fileByte);
        this.fileMapper.insert(fileUploaded);
    }

    public List<File> getFilesByUserId(Integer id) {
        return this.fileMapper.getFilesByUserId(id);
    }

    public void delete(Integer id) {
        this.fileMapper.delete(id);
    }

    public File getFileById(Integer id) {
        return this.fileMapper.getFileById(id);
    }

    public List<File> getFileByFilenameAndUsername(String fileName, Integer userId) {
        return this.fileMapper.getFileByFilenameAndUsername(fileName, userId);
    }


}
