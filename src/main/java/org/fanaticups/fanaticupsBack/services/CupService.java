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

    @Value("${apiRootImagesMinio}")
    private String imageMinioUrl;

    @Autowired
    private UserRepository userRepository;

    public Page<CupDTO> findAllCups(Pageable pageable) {
        Page<CupEntity> pageCupEntity = this.cupRepository.findAll(pageable);
        Page<CupDTO> pageCupDTO = pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));
        pageCupDTO.forEach(this::setImageUrl);
        return pageCupDTO;
    }

    public CupDTO setImageUrl(CupDTO cupDTO){
        String imagePath = this.imageMinioUrl + cupDTO.getUser().getId() + "/" + cupDTO.getName() + "/";
        cupDTO.setImage(imagePath + cupDTO.getImage());
        return cupDTO;
    }

    public Optional<CupDTO> findCupById(Long id) {
        Optional<CupEntity> optionalCupEntity = this.cupRepository.findById(id);
        if (optionalCupEntity.isPresent()) {
            CupEntity cupEntity = optionalCupEntity.get();
            return Optional.of(this.modelMapper.map(cupEntity, CupDTO.class));
        }
        return Optional.empty();
    }

    public Optional<CupDTO> findCupByIdWithImageUrl(Long id) {
        Optional<CupDTO> optionalCupDTO = this.findCupById(id);
        if (optionalCupDTO.isPresent()) {
            return Optional.of(this.setImageUrl(optionalCupDTO.get()));
        }
        return Optional.empty();
    }

    public Optional<CupDTO> add(RequestBodyCreateCup requestBodyCreateCup) {
        Optional<UserEntity> userEntity = this.userRepository.findById(Math.toIntExact(Long.parseLong(requestBodyCreateCup.getUserId())));
        CupDTO cupDTO = null;
        try {
            cupDTO = this.objectMapper.readValue(requestBodyCreateCup.getCup(), CupDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
