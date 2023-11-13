package org.fanaticups.fanaticupsBack.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.security.models.AuthenticationReq;
import org.fanaticups.fanaticupsBack.services.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
    // @CrossOrigin(origins = "*") //this line
    // @RequestMapping(value = "/cups", 
    // produces = "application/json", 
    // method=RequestMethod.POST)
    @PostMapping(value = "/cups")
    public ResponseEntity<CupDTO> create(@RequestBody CupDTO cupDTO){
        System.out.println("desde back: " + cupDTO);
        CupDTO responseCup = this.cupService.add(cupDTO);

        return ResponseEntity.ok(responseCup);
     //return ResponseEntity.ok(cupDTO);
    }
}
