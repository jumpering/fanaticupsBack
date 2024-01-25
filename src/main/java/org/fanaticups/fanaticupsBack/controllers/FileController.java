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
    public ResponseEntity<String> updatePathAndFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") String userId,
                                               @RequestParam("cupName") String cupName,
                                               @RequestParam("cupId") String cupId){
        String originalCupName = this.cupService.findCupById(Long.valueOf(cupId)).get().getName();
        String oldImagePath = userId + "/" + originalCupName + "/";
        String newImagePath = userId + "/" + cupName + "/";
        boolean fileSuccessUploaded = this.fileService.updatePathAndFile(newImagePath, oldImagePath, file);
        if(fileSuccessUploaded){
            return ResponseEntity.ok("File success updated");
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/files/updatePath")
    public ResponseEntity<String> updatePath(@RequestParam("userId") String userId,
                                               @RequestParam("oldPath") String oldPath,
                                               @RequestParam("newPath") String newPath){
        String oldPathToRename = userId + "/" + oldPath + "/";
        String newPathToRename = userId + "/" + newPath + "/";
        boolean fileSuccessUploaded = this.fileService.renamePath(oldPathToRename, newPathToRename);
        if(fileSuccessUploaded){
            return ResponseEntity.ok("File success updated");
        }
        return ResponseEntity.badRequest().build();
    }
}


