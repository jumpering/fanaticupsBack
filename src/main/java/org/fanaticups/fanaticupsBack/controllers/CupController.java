package org.fanaticups.fanaticupsBack.controllers;

import java.util.ArrayList;
import java.util.List;

import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.services.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
}
