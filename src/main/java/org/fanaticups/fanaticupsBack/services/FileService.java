package org.fanaticups.fanaticupsBack.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private String pathDirectory = "///Users/xaviergomezcanals/Documents/Projects/fanaticups/images/";


    public boolean uploadFile(String path, MultipartFile file) {
        String imageName = file.getOriginalFilename();
        String imagePath = this.pathDirectory.concat(path);
        try {
            Files.createDirectories(Paths.get(imagePath));
            Path destination = Paths.get(imagePath + imageName);
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destination,
                    StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.out.println("error uploading image: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePathAndFile(String path) {
        Path directoryPath = Paths.get(this.pathDirectory, path);
        try {
            List<Path> pathsToDelete = Files.walk(directoryPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            pathsToDelete.stream().map(Path::toFile).forEach(File::delete);
            return true;
        } catch (Exception e) {
            System.out.println("error deleting path and file: " + e.getMessage());
            return false;
        }
    }

    public boolean renamePath(String oldPath, String newPath) {
        File oldDirectoryPath = new File(this.pathDirectory + oldPath);
        System.out.println("OLD: " + oldDirectoryPath);
        File newDirectoryPath = new File(this.pathDirectory + newPath);
        System.out.println("NEW: " + newDirectoryPath);
        return oldDirectoryPath.renameTo(newDirectoryPath);
    }

    public boolean updatePathAndFile(String newImagePath, String oldImagePath, MultipartFile file) {
        if (this.deletePathAndFile(oldImagePath)) {
            return this.uploadFile(newImagePath, file);
        }
        return false;
    }
}
