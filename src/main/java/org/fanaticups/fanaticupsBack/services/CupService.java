package org.fanaticups.fanaticupsBack.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.fanaticups.fanaticupsBack.models.RequestBodyCreateCup;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.CupRepository;
import org.fanaticups.fanaticupsBack.models.CupDTO;

import org.modelmapper.ModelMapper;

@Service
public class CupService {

    @Autowired
    private CupRepository cupRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ObjectMapper objectMapper = new ObjectMapper(); //Jackson mapper

    @Value("${apiMinio}")
    private String imageMinioUrl;

    @Autowired
    private UserRepository userRepository;

//    public List<CupDTO> findAllCups(){
//        List<CupEntity> cupsEntities;
//        List<CupDTO> cupsDTO = new ArrayList<CupDTO>();
//        cupsEntities = this.cupRepository.findAll();
//        cupsEntities.forEach(element -> {
//            String imagePath = element.getUser().getId() + "/" + element.getName() + "/";
//            element.setImage(imagePath + element.getImage());
//        });
//        cupsEntities.forEach(element -> cupsDTO.add(
//            this.modelMapper.map(element, CupDTO.class)
//        ));
//        return cupsDTO;
//    }


    //THIS IS WORKING NOW !!!!!!
//    public Page<CupDTO> findAllCups(Pageable pageable){
//        Page<CupEntity> pageCupEntity = this.cupRepository.findAll(pageable);
//        pageCupEntity.forEach(element -> {
//            String imagePath = element.getUser().getId() + "/" + element.getName() + "/";
//            element.setImage(imagePath + element.getImage());
//        });
//        return pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));
//    }

//TRY WITH S3 MINIO ON MY VPS IONOS
    public Page<CupDTO> findAllCups(Pageable pageable){
        Page<CupEntity> pageCupEntity = this.cupRepository.findAll(pageable);

        try {
//            MinioClient minioClient =
//                    MinioClient.builder()
//                            .endpoint("http://5.250.184.31",9000,false)
//                            .credentials("DjjGWp1QpWL87Q9Z7fJT", "BaoeKiv67fBHJmwJan5se7NXcLyImYeAUeMRNKWY")
//                            .build();

            pageCupEntity.forEach(element -> {
            String imagePath = this.imageMinioUrl + element.getUser().getId() + "/" + element.getName() + "/";
            element.setImage(imagePath + element.getImage());
        });
        return pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));

//            List<Bucket> allBuckets = new ArrayList<>();
//            allBuckets = minioClient.listBuckets();
//            System.out.println("TEST IF EXIST BUCKET: " + minioClient.bucketExists(BucketExistsArgs.builder().bucket("images").build()));
//            for(Bucket bucket: allBuckets){
//                System.out.println("BUCKET NAME: " + bucket.name());
//            }

//            String url =
//                    minioClient.getPresignedObjectUrl(
//                            GetPresignedObjectUrlArgs.builder()
//                                    .method(Method.GET)
//                                    .bucket("fanaticups-bucket-1-AAA")
//                                    .object("Bilma.jpg")
//                                    .expiry(2, TimeUnit.HOURS)
//                                    .build());
//            System.out.println("URL DE LA IMAGEN?¿?¿: " + url);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CupDTO> findCupById(Long id){
        Optional<CupEntity> optionalCupEntity = this.cupRepository.findById(id);
        if(optionalCupEntity.isPresent()){
            CupEntity cupEntity = optionalCupEntity.get();
            String imagePath = this.imageMinioUrl  + cupEntity.getUser().getId() + "/" + cupEntity.getName() + "/";
            cupEntity.setImage(imagePath + cupEntity.getImage());
            return Optional.of(this.modelMapper.map(cupEntity, CupDTO.class));
        }
        return Optional.empty();
    }

    public Optional<CupDTO> add(RequestBodyCreateCup requestBodyCreateCup) throws JsonProcessingException {
        Optional<UserEntity> userEntity = this.userRepository.findById(Math.toIntExact(Long.parseLong(requestBodyCreateCup.getUserId())));
        CupDTO cupDTO = this.objectMapper.readValue(requestBodyCreateCup.getCup(), CupDTO.class);
        cupDTO.setUser(userEntity.get());
        CupEntity cupEntity = this.cupRepository.save(this.modelMapper.map(cupDTO, CupEntity.class));
        cupDTO = this.modelMapper.map(cupEntity, CupDTO.class);
        return Optional.of(cupDTO);
    }

    public Boolean existCupByUserIdAndCupName(String userId, String name) {
        return this.cupRepository.existsByUser_IdAndName(Long.valueOf(userId), name);
    }

    public void delete(Long id) {
        this.cupRepository.deleteById(id);
    }

//    public Optional<CupDTO> updateCupById(RequestBodyUpdateCup requestBodyUpdateCup) throws JsonProcessingException {
//        CupDTO cupDTO = this.objectMapper.readValue(requestBodyUpdateCup.getCupId(), CupDTO.class);
//        CupEntity cupEntity = this.modelMapper.map(cupDTO, CupEntity.class);
//        this.cupRepository.update(cupEntity);
//        return Optional.empty();
//    }

    public Optional<CupDTO> update(Long id, String cupDTO) throws JsonProcessingException {
        CupEntity currentCupEntity = this.cupRepository.findById(id).get();
        CupDTO mappedCupDTO = this.objectMapper.readValue(cupDTO, CupDTO.class);
        currentCupEntity.setName(mappedCupDTO.getName());
        currentCupEntity.setOrigin(mappedCupDTO.getOrigin());
        currentCupEntity.setDescription(mappedCupDTO.getDescription());
        currentCupEntity.setPrice(mappedCupDTO.getPrice());
        currentCupEntity.setImage(mappedCupDTO.getImage());
        CupEntity updatedEntity = this.cupRepository.save(currentCupEntity);
        return Optional.of(this.modelMapper.map(updatedEntity, CupDTO.class));
    }
}
