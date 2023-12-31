package org.fanaticups.fanaticupsBack.controllers;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.models.RequestBodyCreateCup;
import org.fanaticups.fanaticupsBack.models.RequestBodyUpdateCup;
import org.fanaticups.fanaticupsBack.services.CupService;
import org.fanaticups.fanaticupsBack.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class CupController {

    @Autowired
    private CupService cupService;

    @Autowired
    private FileService fileService;

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
    public ResponseEntity<List<CupDTO>> findAllCups() {
        List<CupDTO> cupsDTOList = this.cupService.findAllCups();
        return cupsDTOList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cupsDTOList);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/cups/{id}")
    public ResponseEntity<CupDTO> findCupById(@PathVariable Long id) {
        Optional<CupDTO> optionalCupDTO = this.cupService.findCupById(id);
        return optionalCupDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/cups/existCupNameByUserId")
    public ResponseEntity<Boolean> existCupByUserIdAndCupName(
            @RequestParam("userId") String userId,
            @RequestParam("cupName") String cupName) {
        return ResponseEntity.ok(this.cupService.existCupByUserIdAndCupName(userId, cupName));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/cups")
    public ResponseEntity<CupDTO> create(@RequestBody RequestBodyCreateCup requestBodyCreateCup) throws JsonProcessingException {
        Optional<CupDTO> optionalCupDTO = this.cupService.add(requestBodyCreateCup);
        return optionalCupDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(value = "/cups/{id}")
    public ResponseEntity<Void> deleteCup(@PathVariable Long id) { //¿es necesario buscar el cup con findCupById?
        Optional<CupDTO> optionalCupDTO = this.cupService.findCupById(id);
        if (optionalCupDTO.isPresent()){
            CupDTO cupDTO = optionalCupDTO.get();
            String path = cupDTO.getUser().getId() + "/" + cupDTO.getName() + "/";
            if (this.fileService.deletePathAndFile(path, cupDTO.getImage())) {
                this.cupService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204 response
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/cup/{id}")
    public ResponseEntity<CupDTO> updateCup(@RequestBody RequestBodyUpdateCup requestBodyUpdateCup) throws JsonProcessingException {
        Optional<CupDTO> optionalCupDTO = this.cupService.updateCupById(requestBodyUpdateCup);
        return optionalCupDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
