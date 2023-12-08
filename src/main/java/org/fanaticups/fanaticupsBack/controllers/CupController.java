package org.fanaticups.fanaticupsBack.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.models.RequestBodyCreateCup;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.fanaticups.fanaticupsBack.security.models.AuthenticationReq;
import org.fanaticups.fanaticupsBack.services.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
public class CupController {

    @Autowired
    private CupService cupService;

    @Autowired
    private UserRepository userRepository;

    private String pathDirectory = "///Users/xaviergomezcanals/Documents/Projects/fanaticups/images/";
    
    @Operation(summary = "Get all cups")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", 
                        description = "Found cups", 
                        content = { 
                                @Content(mediaType = "application/json", 
                                    schema = @Schema(implementation = CupDTO.class))}
                                    )})
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/cups")
    public ResponseEntity<List<CupDTO>> findAllCups(){
        System.out.println("TRAZA");
        List<CupDTO> cupsDTOList = this.cupService.findAllCups();
        if(cupsDTOList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cupsDTOList);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/cups/{id}")
    public ResponseEntity<CupDTO> findCupById(@PathVariable Long id){

        CupDTO cupDTO = this.cupService.findCupById(id);
        if(cupDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cupDTO);
    }

     @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/cups")
    public ResponseEntity<CupDTO> create(@RequestBody RequestBodyCreateCup requestBodyCreateCup) throws IOException {
         System.out.println("id:" + requestBodyCreateCup.getUserId() + " ::: " + " cup: " + requestBodyCreateCup.getCup());
         Optional<UserEntity> userEntity = this.userRepository.findById(Math.toIntExact(Long.parseLong(requestBodyCreateCup.getUserId())));
         ObjectMapper objectMapper = new ObjectMapper();
         CupDTO cupDTO = objectMapper.readValue(requestBodyCreateCup.getCup(), CupDTO.class);
         cupDTO.setUser(userEntity.get());
//         MultipartFile file = requestBodyCreateCup.getFile();
//         String fullFile = file.getOriginalFilename();
//         //cupDTO.setImage(fullFile);
//         //Path destination = Paths.get("src/main/resources/images/" + fullFile);
//         Path destination = Paths.get(pathDirectory + fullFile);
//         InputStream inputStream = file.getInputStream();
//         Files.copy(inputStream, destination,
//                 StandardCopyOption.REPLACE_EXISTING);
//
         CupDTO responseCup = this.cupService.add(cupDTO);
//
//
        return ResponseEntity.ok(responseCup);
    }

    @GetMapping(value = "/cups/user/{id}")
    public ResponseEntity<List<CupDTO>> getUserCupsList(@PathVariable Long id){
        Optional<List<CupDTO>> cupDTOListOptional = this.cupService.findAllCupsFromUser(id);
        if(!cupDTOListOptional.isEmpty()){
            return ResponseEntity.ok(cupDTOListOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
}
