package org.fanaticups.fanaticupsBack.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioService {

    private MinioClient minioClient;
    private final String accesKeyId = "DjjGWp1QpWL87Q9Z7fJT";
    private final String secretAccessKey = "BaoeKiv67fBHJmwJan5se7NXcLyImYeAUeMRNKWY";
    private final String apiMinioUrl;
    private final String baseImagePath = "/fanaticups/";
    private final String bucketName = "images";

    public MinioService(@Value("${apiBaseUrlMinio}") String apiMinioUrl) {
        //this.apiMinioUrl = apiMinioUrl;
        this.apiMinioUrl = "http://172.17.0.5:9000";
        try {
            this.minioClient = MinioClient.builder()
                    .endpoint(this.apiMinioUrl)
                    .credentials(this.accesKeyId, this.secretAccessKey)
                    .build();
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
