package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final HashService hashService;
    private final EncryptionService encryptionService;

    @PostConstruct
    public void PostConstruct() {
        System.out.println("Creating credentialService bean");
    }

    public CredentialService(CredentialMapper credentialMapper, HashService hashService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.hashService = hashService;
        this.encryptionService = encryptionService;
    }

    public void createAndUpdateCredential(Credential credential) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = encryptionService.encryptValue(credential.getPassword(), encodedSalt);
        credential.setKey(encodedSalt);
        credential.setPassword(hashedPassword);
        if ( credential.getCredentialId() != null ) {
            this.credentialMapper.update(credential);
        } else {
            this.credentialMapper.insert(credential);
        }

    }

    public List<Credential> getCredentialsByUserId(Integer id) {
        List<Credential> credentials = this.credentialMapper.getCredentialByUserId(id);
        return credentials.stream().map(credential -> {
            String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setDecryptedPassword(decryptedPassword);
            return credential;
        }).collect(Collectors.toList());
    }

    public void delete(Integer id) {
        this.credentialMapper.delete(id);
    }
}
