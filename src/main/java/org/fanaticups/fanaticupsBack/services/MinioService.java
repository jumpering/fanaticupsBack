package org.fanaticups.fanaticupsBack.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    private MinioClient minioClient;
    //private final String accesKeyId = "Bearer DjjGWp1QpWL87Q9Z7fJT";
    private final String accesKeyId = "DjjGWp1QpWL87Q9Z7fJT";
    private final String secretAccessKey = "BaoeKiv67fBHJmwJan5se7NXcLyImYeAUeMRNKWY";
    private final String apiMinioUrl = "http://172.17.0.5:9000"; //ESTE FUNCIONA!!!!!!!!!
    //private final String apiMinioUrl = "//images";
    private final String baseImagePath = "/fanaticups/";
    private final String bucketName = "images";

    //public MinioService(@Value("${apiBaseUrlMinio}") String apiMinioUrl) {
    public MinioService() {
        //this.apiMinioUrl = apiMinioUrl;
        //this.apiMinioUrl = "172.17.0.5:9000";
        try {
            this.minioClient = MinioClient.builder()
                    .endpoint(this.apiMinioUrl)
                    .credentials(this.accesKeyId, this.secretAccessKey)
                    .build();
            System.out.println("minio registrado!!! " + this.minioClient);
        } catch (Exception e) {
            System.out.println("Error on authenticate minio and init: " + e);
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
}
