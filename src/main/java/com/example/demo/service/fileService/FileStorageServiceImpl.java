package com.example.demo.service.fileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService{

    private final Path uploadDirectory;

    public FileStorageService(
            @Value("${file.upload-dir}") String uploadDir) {

        this.uploadDirectory = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not create upload directory",
                    e
            );
        }
    }

    public String save(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "File is empty"
            );
        }

        String originalFilename =
                file.getOriginalFilename();

        if (originalFilename == null) {
            throw new IllegalArgumentException(
                    "Invalid filename"
            );
        }

        String extension = "";

        int dotIndex =
                originalFilename.lastIndexOf(".");

        if (dotIndex >= 0) {
            extension =
                    originalFilename.substring(dotIndex);
        }

        String filename =
                UUID.randomUUID() + extension;

        Path target =
                uploadDirectory
                        .resolve(filename)
                        .normalize();

        if (!target.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Invalid file path"
            );
        }

        try {

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return filename;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not save file",
                    e
            );
        }
    }

    public void delete(String filename) {

        if (filename == null || filename.isBlank()) {
            return;
        }

        Path file =
                uploadDirectory
                        .resolve(filename)
                        .normalize();

        if (!file.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException(
                    "Invalid file path"
            );
        }

        try {

            Files.deleteIfExists(file);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not delete file: " + filename,
                    e
            );
        }
    }
}