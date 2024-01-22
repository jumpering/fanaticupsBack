package org.fanaticups.fanaticupsBack.controllers;

import org.fanaticups.fanaticupsBack.services.CupService;
import org.fanaticups.fanaticupsBack.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private CupService cupService;

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

    @PutMapping("/files")
    public ResponseEntity<String> updateUpload(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") String userId,
                                               @RequestParam("cupName") String cupName,
                                               @RequestParam("cupId") String cupId){
        //debería saber el nombre anterior de la taza para saber el nombre del directorio que tengo que renombrar.
        String originalCupName = this.cupService.findCupById(Long.valueOf(cupId)).get().getName();
        String oldImagePath = userId + "/" + originalCupName + "/";
        String newImagePath = userId + "/" + cupName + "/";
        boolean fileSuccessUploaded = this.fileService.updateFile(newImagePath, oldImagePath, file);
        return ResponseEntity.ok("File success updated");
    }
}


