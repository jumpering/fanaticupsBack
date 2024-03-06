package org.fanaticups.fanaticupsBack.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.fanaticups.fanaticupsBack.models.RequestBodyCreateCup;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;

import org.fanaticups.fanaticupsBack.utils.FileComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

    public CupDTO setImageUrl(CupDTO cupDTO) {
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

    public Optional<CupDTO> updateV2(String cup, MultipartFile image) {
        CupDTO mappedCupDTO = null;
        try {
            mappedCupDTO = this.objectMapper.readValue(cup, CupDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Long id = mappedCupDTO.getId();
        Long userId = mappedCupDTO.getUser().getId();
        String cupName = mappedCupDTO.getName();
        String cupImage = mappedCupDTO.getImage();
        //CupDTO previusCupDTO = this.findCupById(id).get();
        CupEntity previusCupEntity = this.cupRepository.findById(id).get();
        CupEntity updatedCupEntity;
        if (image != null) {
            if (cupName.equals(previusCupEntity.getName())) {
                InputStream previusImage = this.minioService.downloadFile(userId + "/" + cupName + "/" + cupImage);
                try {
                    File previusImageFile = File.createTempFile("temp-", null);
                    Files.copy(previusImage, previusImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    InputStream incomingImage = image.getInputStream();
                    File incomingImageFile = File.createTempFile("temp2-", null);
                    Files.copy(incomingImage, incomingImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    if (FileComparator.compareImages(previusImageFile, incomingImageFile)) {
                        System.out.println("IMAGENES SON IDENTICAS Y NOMBRE DE LA CUP TAMBIEN ! ");
                        //pues solo actualizo la cup y la imagen no la toco
                        //¿puedo usar model mapper?
                        previusCupEntity.setName(mappedCupDTO.getName());
                        previusCupEntity.setOrigin(mappedCupDTO.getOrigin());
                        previusCupEntity.setDescription(mappedCupDTO.getDescription());
                        previusCupEntity.setPrice(mappedCupDTO.getPrice());
                        updatedCupEntity = this.cupRepository.save(previusCupEntity);//ojo con los campos vacios de imagen
                        return Optional.of(this.modelMapper.map(updatedCupEntity, CupDTO.class));

                    } else {
                        System.out.println("IMAGENES SON DIFERENTES PERO LA CUP SE LLAMA IGUAL: ");
                        //pues subo la imagen nuevo (borro primero la antigua)
                        //y actualizo la cup
                        boolean createdNewImage = false;
                        boolean deletedOldImage = this.minioService.deletePathAndFile(userId + "/" + cupName + "/" + cupImage);
                        if (deletedOldImage) {
                            System.out.println("imagen antigua borrada");
                            createdNewImage = this.minioService.uploadFile(userId + "/" + cupName + "/", image);
                        }
                        if (createdNewImage) {
                            System.out.println("imagen nueva creada");
                            previusCupEntity.setName(mappedCupDTO.getName());
                            previusCupEntity.setOrigin(mappedCupDTO.getOrigin());
                            previusCupEntity.setDescription(mappedCupDTO.getDescription());
                            previusCupEntity.setPrice(mappedCupDTO.getPrice());
                            previusCupEntity.setImage(image.getOriginalFilename());
                            updatedCupEntity = this.cupRepository.save(previusCupEntity);//ojo con los campos vacios de imagen
                            return Optional.of(this.modelMapper.map(updatedCupEntity, CupDTO.class));
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                InputStream previusImage = this.minioService.downloadFile(userId + "/" + previusCupEntity.getName() + "/" + cupImage);
                File previusImageFile = null;
                try {
                    previusImageFile = File.createTempFile("temp-", null);
                    Files.copy(previusImage, previusImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    InputStream incomingImage = image.getInputStream();
                    File incomingImageFile = File.createTempFile("temp2-", null);
                    Files.copy(incomingImage, incomingImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    if (FileComparator.compareImages(previusImageFile, incomingImageFile)) {
                        System.out.println("IMAGENES IGUALES y NOMBRES DIFERENTES");
                        //crear nuevo directorio
                        //mover a nuevo
                        //borrar antiguo directorio e imagen
                        //actualizo cup
                        boolean createdDirectory = this.minioService.createDirectory(userId + "/" + cupName + "/");
                        if (createdDirectory) {
                            System.out.println("nuevo directorio creado");
                            boolean movedImage = this.minioService.moveFile(userId + "/" + previusCupEntity.getName() + "/" + cupImage, userId + "/" + cupName + "/" + cupImage);
                            if (movedImage) {
                                System.out.println("imagen movida entre directorios");
                                boolean deletedOldFile = this.minioService.deletePathAndFile(userId + "/" + previusCupEntity.getName() + "/" + cupImage);
                                if (deletedOldFile) {
                                    System.out.println("ruta antigua borrada");
                                    previusCupEntity.setName(mappedCupDTO.getName());
                                    previusCupEntity.setOrigin(mappedCupDTO.getOrigin());
                                    previusCupEntity.setDescription(mappedCupDTO.getDescription());
                                    previusCupEntity.setPrice(mappedCupDTO.getPrice());
                                    previusCupEntity.setImage(mappedCupDTO.getImage());
                                    updatedCupEntity = this.cupRepository.save(previusCupEntity);//ojo con los campos vacios de imagen
                                    return Optional.of(this.modelMapper.map(updatedCupEntity, CupDTO.class));
                                }
                            }
                        }
                    } else {
                        System.out.println("IMAGENES DIFERENTES y NOMBRES DIFERENTES");
                        //borro antigua imagen y path
                        //creo nueva imagen y path
                        //actualizo cup
                        boolean deletedOldFile = this.minioService.deletePathAndFile(userId + "/" + previusCupEntity.getName() + "/" + previusCupEntity.getImage());
                        if (deletedOldFile) {
                            System.out.println("se ha borrado la antigua ruta e imagen");
                            boolean createdNewFile = this.minioService.uploadFile(userId + "/" + cupName + "/", image);
                            if (createdNewFile) {
                                System.out.println("se ha creado la nueva ruta e imagen");
                                previusCupEntity.setName(mappedCupDTO.getName());
                                previusCupEntity.setOrigin(mappedCupDTO.getOrigin());
                                previusCupEntity.setDescription(mappedCupDTO.getDescription());
                                previusCupEntity.setPrice(mappedCupDTO.getPrice());
                                previusCupEntity.setImage(image.getOriginalFilename());
                                updatedCupEntity = this.cupRepository.save(previusCupEntity);//ojo con los campos vacios de imagen
                                return Optional.of(this.modelMapper.map(updatedCupEntity, CupDTO.class));
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } else {
            System.out.println("NO EXISTE IMAGE CUP");
                if (cupName.equals(previusCupEntity.getName())) {
                    System.out.println("NO HAY IMAGEN y NOMBRES IGUALES");
                    //actualizo cup, misma imagen
                    previusCupEntity.setName(mappedCupDTO.getName());
                    previusCupEntity.setOrigin(mappedCupDTO.getOrigin());
                    previusCupEntity.setDescription(mappedCupDTO.getDescription());
                    previusCupEntity.setPrice(mappedCupDTO.getPrice());
                    updatedCupEntity = this.cupRepository.save(previusCupEntity);//ojo con los campos vacios de imagen
                    return Optional.of(this.modelMapper.map(updatedCupEntity, CupDTO.class));

                } else{
                    System.out.println("NO HAY IMAGEN y NOMBRES DIFERENTES");
                    //creo nuevo directorio
                    //copio imagen a nuevo directorio
                    //borro antiguo directorio
                    //actualizo cup
                    boolean createdDirectory = this.minioService.createDirectory(userId + "/" + cupName + "/");
                    if (createdDirectory) {
                        System.out.println("nuevo directorio creado");
                        boolean movedImage = this.minioService.moveFile(userId + "/" + previusCupEntity.getName() + "/" + cupImage, userId + "/" + cupName + "/" + cupImage);
                        if (movedImage) {
                            System.out.println("imagen movida entre directorios");
                            boolean deletedOldFile = this.minioService.deletePathAndFile(userId + "/" + previusCupEntity.getName() + "/" + cupImage);
                            if (deletedOldFile) {
                                System.out.println("ruta antigua borrada");
                                previusCupEntity.setName(mappedCupDTO.getName());
                                previusCupEntity.setOrigin(mappedCupDTO.getOrigin());
                                previusCupEntity.setDescription(mappedCupDTO.getDescription());
                                previusCupEntity.setPrice(mappedCupDTO.getPrice());
                                previusCupEntity.setImage(previusCupEntity.getImage());
                                updatedCupEntity = this.cupRepository.save(previusCupEntity);//ojo con los campos vacios de imagen
                                return Optional.of(this.modelMapper.map(updatedCupEntity, CupDTO.class));
                            }
                        }
                    }
                }
        }
        return Optional.empty();
    }
}
