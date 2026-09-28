package com.example.demo.service.fileService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    String save(MultipartFile file);
    void delete(String filename);
}
