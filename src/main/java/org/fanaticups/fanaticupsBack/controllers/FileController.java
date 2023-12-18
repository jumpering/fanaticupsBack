package org.fanaticups.fanaticupsBack.controllers;

import org.fanaticups.fanaticupsBack.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("userId") String userId,
                                 @RequestParam("cupName") String cupName) {
        String imagePath = userId + "/" + cupName + "/";
        boolean fileSuccessUploaded = this.fileService.uploadFile(imagePath, file);
        if(fileSuccessUploaded){
            return ResponseEntity.ok("File success uploaded");
        }
        return ResponseEntity.badRequest().build();
    }
}
