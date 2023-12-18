package org.fanaticups.fanaticupsBack.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private String pathDirectory = "///Users/xaviergomezcanals/Documents/Projects/fanaticups/images/";

    public boolean uploadFile(String path, MultipartFile file){
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

//    public boolean deleteFile(String path, String fileName){
//        String imagePath = this.pathDirectory.concat(path);
//        try {
//            Files.deleteIfExists(Paths.get(fileName));
//            return true;
//        } catch (IOException e) {
//            System.out.println("error deleting image: " + e.getMessage());
//            return false;
//        }
//    }

    public boolean deletePathAndFiles(String path){
        String imagePath = this.pathDirectory.concat(path);
        String baseDirectory = "/user/xa/Documents/Projects/miProyecto/images";
        String subdirectoryPath = "1/Hola que tal";

        try {
            // Combina las rutas para obtener la ruta completa del directorio a eliminar
            //Path directoryPath = Paths.get(baseDirectory, subdirectoryPath);
            Path directoryPath = Paths.get(this.pathDirectory, path);
            System.out.println("PATH DE ENTRADA: " + path);


            // Recopilar elementos para eliminar
            List<Path> pathsToDelete = Files.walk(directoryPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .peek(System.out::println)
                    .collect(Collectors.toList());


//            Files.walk(directoryPath)
//                    .sorted(Comparator.reverseOrder())
//                    .map(Path::toFile)
//                    .forEach(File::delete);

            // Eliminar archivos y directorios
//            for (Path e : pathsToDelete) {
//                Files.deleteIfExists(e);
//            }
            System.out.println("TRAZA: " + pathsToDelete);
            return true;
        } catch (IOException e) {
            System.out.println("error deleting path: " + e.getMessage());
            return false;
        }
    }
}
