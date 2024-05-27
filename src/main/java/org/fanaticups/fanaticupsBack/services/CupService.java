package org.fanaticups.fanaticupsBack.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;

import org.fanaticups.fanaticupsBack.utils.FileComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.CupRepository;
import org.fanaticups.fanaticupsBack.models.CupDTO;

import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private MinioService minioService;

    public Page<CupDTO> findAllCups(Pageable pageable) {
        Page<CupEntity> pageCupEntity = this.cupRepository.findAll(pageable);
        Page<CupDTO> pageCupDTO = pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));
        pageCupDTO.forEach(this::setImageUrl);
        return pageCupDTO;
    }

    public Page<CupDTO> findAllCupsWithSearchName(Pageable pageable, String searchName) {
        Page<CupEntity> pageCupEntity = this.cupRepository.findAllByNameContaining(pageable, searchName);
        Page<CupDTO> pageCupDTO = pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));
        pageCupDTO.forEach(this::setImageUrl);
        return pageCupDTO;
    }

    public CupDTO setImageUrl(CupDTO cupDTO) {
        String imagePath = this.imageMinioUrl + cupDTO.getUser().getId() + "/" + cupDTO.getId() + "/";
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

    public Optional<CupDTO> add(String userId, String cup) {
        Optional<UserEntity> userEntity = this.userRepository.findById(Long.parseLong(userId));
        CupDTO cupDTO = null;
        try {
            cupDTO = this.objectMapper.readValue(cup, CupDTO.class);
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

    public void delete(Long cupId) {
        this.userRepository.deleteCupFromFavorites(cupId);
        this.cupRepository.deleteById(cupId);
    }

    public Optional<CupDTO> update(String cup, MultipartFile image) {
        CupDTO mappedCupDTO = null;
        try {
            mappedCupDTO = this.objectMapper.readValue(cup, CupDTO.class);
        } catch (JsonProcessingException e) {
            System.out.println("Error mapping from JSON: " + e);
        }
        CupEntity previusCupEntity = this.cupRepository.findById(mappedCupDTO.getId()).get();
        String previusImageName = previusCupEntity.getImage();
        CupEntity updatedCupEntity;
        previusCupEntity = this.modelMapper.map(mappedCupDTO, CupEntity.class);
        if (image != null) {
            InputStream previusImage = this.minioService.downloadFile(mappedCupDTO.getUser().getId() + "/" + mappedCupDTO.getId() + "/" + previusImageName);
            if (this.isEqualImages(previusImage, image)) {
                previusCupEntity.setImage(previusImageName);
            } else {
                boolean deletedOldImage = this.minioService.deletePathAndFile(mappedCupDTO.getUser().getId() + "/" + mappedCupDTO.getId() + "/" + previusImageName);
                boolean createdNewImage = this.minioService.uploadFile(mappedCupDTO.getUser().getId() + "/" + mappedCupDTO.getId() + "/", image);
                previusCupEntity.setImage(image.getOriginalFilename());
                if (!deletedOldImage || !createdNewImage) {
                    System.out.println("Error changing image");
                }
            }
        } else {
            previusCupEntity.setImage(previusImageName);
        }
        updatedCupEntity = this.cupRepository.save(previusCupEntity);
        return Optional.of(this.modelMapper.map(updatedCupEntity, CupDTO.class));
    }

    private boolean isEqualImages(InputStream previusImage, MultipartFile image) {
        boolean isEquals = false;
        try {
            InputStream incomingImage = image.getInputStream();
            File previusImageFile = File.createTempFile("temp-", null);
            Files.copy(previusImage, previusImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            File incomingImageFile = File.createTempFile("temp2-", null);
            Files.copy(incomingImage, incomingImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            isEquals = FileComparator.isEqualImages(previusImageFile, incomingImageFile);
        } catch (IOException e) {
            System.out.println("Error comparing files: " + e);
        }
        return isEquals;
    }

    public Page<CupDTO> findAllFromUserId(Long userId, Pageable pageable) {
        Optional<UserEntity> optionalUser = this.userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Page<CupEntity> pageCupEntity = this.cupRepository.findAllByUser(optionalUser.get(), pageable);
            Page<CupDTO> pageCupDTO = pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));
            pageCupDTO.forEach(this::setImageUrl);
            return pageCupDTO;
        }
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
}