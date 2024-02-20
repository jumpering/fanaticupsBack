package org.fanaticups.fanaticupsBack.services;

import io.minio.MinioClient;
import org.springframework.stereotype.Service;

@Service
public class MinioService {

    private MinioClient minioClient;
    private final String accesKeyId = "Bearer DjjGWp1QpWL87Q9Z7fJT";
    private final String secretAccessKey = "BaoeKiv67fBHJmwJan5se7NXcLyImYeAUeMRNKWY";
    private final String apiMinioUrl = "http://5.250.184.31:9000"; // Reemplaza con la URL de tu servidor MinIO

    public MinioService(){

    }

    public void authenticate(){
        this.minioClient.builder()
                .endpoint(this.apiMinioUrl)
                .credentials(this.accesKeyId, this.accesKeyId)
                .build();
    }

    public String getImageUrl(String imageName){
        this.authenticate();

        return this.apiMinioUrl + imageName;
    }
}
