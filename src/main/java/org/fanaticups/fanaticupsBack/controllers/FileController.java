package org.fanaticups.fanaticupsBack.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
public class FileController {

    private String pathDirectory = "///Users/xaviergomezcanals/Documents/Projects/fanaticups/images/";

    @PostMapping("/files")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("userId") String userId,
                                 @RequestParam("cupName") String cupName) throws IOException { //todo usar userId jerarquia carpetas?
        String fullFile = file.getOriginalFilename();
        //Path destination = Paths.get("src/main/resources/images/" + fullFile);
        Path destination = Paths.get(pathDirectory + fullFile);

        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, destination,
                StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok("File success uploaded");
    }
}
