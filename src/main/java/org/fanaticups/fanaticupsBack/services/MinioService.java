package org.fanaticups.fanaticupsBack.services;

import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class MinioService {

    private MinioClient minioClient;
    private final String accesKeyId = "DjjGWp1QpWL87Q9Z7fJT";//extract to application.propierties
    private final String secretAccessKey = "BaoeKiv67fBHJmwJan5se7NXcLyImYeAUeMRNKWY";//extract to application.propierties
    private final String apiBaseUrlMinio;
    private final String baseImagePath = "/fanaticups/";

//    @Value("${apiRootImagesMinio}")
//    private String apiRootImagesMinio;
    private final String bucketName = "images";

    public MinioService(@Value("${apiBaseUrlMinio}") String apiBaseUrlMinio) {
        this.apiBaseUrlMinio = apiBaseUrlMinio;
        try {
            this.minioClient = MinioClient.builder()
                    .endpoint(this.apiBaseUrlMinio)
                    .credentials(this.accesKeyId, this.secretAccessKey)
                    .build();
        } catch (Exception e) {
            System.out.println("Error on authenticate minio: " + e);
        }
    }

    public boolean uploadFile(String path, MultipartFile file) {
        String imageName = file.getOriginalFilename();
        try {
            this.minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.bucketName)
                            .object(this.baseImagePath + path + imageName)
                            .stream(file.getInputStream(), -1, 10485760)
                            .build()
            );
            return true;
        } catch (Exception e) {
            System.out.println("Error when try to upload image on Minio S3 Server: " + e);
            return false;
        }
    }

    public boolean deletePathAndFile(String path){
        String imagePath = this.baseImagePath + path;
        try {
            this.minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(this.bucketName)
                            .object(imagePath)
                            .build()
            );
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting image path and file: " + e);
            return false;
        }
    }

//    public boolean moveFile(String pathOrigin, String pathDestination) { //full path with image needed
//        try {
//            minioClient.copyObject(
//                    CopyObjectArgs.builder()
//                            .bucket(this.bucketName)
//                            .object(this.baseImagePath + pathDestination)
//                            .source(
//                                    CopySource.builder()
//                                            .bucket(this.bucketName)
//                                            .object(this.baseImagePath + pathOrigin)
//                                            .build())
//                            .build());
//            return true;
//        } catch (Exception e) {
//            System.out.println("Error moving file between directories: " + e);
//            return false;
//        }
//    }

    public InputStream downloadFile(String path){
        InputStream stream;
        try {
            stream = this.minioClient.getObject(GetObjectArgs.builder()
                    .bucket(this.bucketName)
                    .object(this.baseImagePath + path)
                    .build());
        } catch (Exception e) {
            System.out.println("Error downloading file: " + path);
            throw new RuntimeException(e);
        }
        return stream;
    }

//    public boolean createDirectory(String path){
//        try {
//            this.minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(this.bucketName)
//                            .object(this.baseImagePath + path)
//                            .stream(
//                                    new ByteArrayInputStream(new byte[] {}), 0, -1)
//                            .build());
//            return true;
//        } catch (Exception e) {
//            System.out.println("Error creating directory in MinioS3: " + e);
//            return false;
//        }
//    }
}
