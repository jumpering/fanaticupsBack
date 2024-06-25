package org.fanaticups.fanaticupsBack.controllers;

import java.util.Optional;

import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.services.CategoryService;
import org.fanaticups.fanaticupsBack.services.ChatService;
import org.fanaticups.fanaticupsBack.services.CupService;
import org.fanaticups.fanaticupsBack.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class CupController {

    @Autowired
    private CupService cupService;

    @Autowired
    private MinioService minioService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all cups pageable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found cups",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CupDTO.class))}
            )})
    @CrossOrigin(origins = "http://localhost:4200")
    //@CrossOrigin
    @GetMapping(value = "/cups")
    public ResponseEntity<Page<CupDTO>> findAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC ,page = 0, size = 12) Pageable pageable) {
        Page<CupDTO> cupsDTOList = this.cupService.findAllCups(pageable);
        return cupsDTOList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cupsDTOList);
    }

    @Operation(summary = "Get all cups pageable with name search String")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found cups",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CupDTO.class))}
            )})
    @CrossOrigin(origins = "http://localhost:4200")
    //@CrossOrigin
    @GetMapping(value = "/cups/name/{cupName}")
    public ResponseEntity<Page<CupDTO>> findAllWithSearchStringName(@PathVariable String cupName, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 12) Pageable pageable) {
        Page<CupDTO> cupsDTOList = this.cupService.findAllCupsWithSearchName(pageable, cupName);
        return cupsDTOList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cupsDTOList);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/cups/userId/{userId}")
    public ResponseEntity<Page<CupDTO>> findAllFromUserId(@PathVariable Long userId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 12) Pageable pageable){
        Page<CupDTO> cupsDTOList = this.cupService.findAllFromUserId(userId, pageable);
        return cupsDTOList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cupsDTOList);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/cups/{id}")
    public ResponseEntity<CupDTO> findById(@PathVariable Long id) {
        Optional<CupDTO> optionalCupDTO = this.cupService.findCupByIdWithImageUrl(id);
        return optionalCupDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/cups/existCupNameByUserId")
    public ResponseEntity<Boolean> existCupByUserIdAndCupName(
            @RequestParam("userId") String userId,
            @RequestParam("cupName") String cupName) {
        return ResponseEntity.ok(this.cupService.existCupByUserIdAndCupName(userId, cupName));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/cups")
    public ResponseEntity<CupDTO> create(@RequestParam("file") MultipartFile file,
                                         @RequestParam("userId") String userId,
                                         @RequestParam("cup") String cup) {
        Optional<CupDTO> optionalCupDTO = this.cupService.add(userId, cup);
        if(optionalCupDTO.isPresent()){
            CupDTO cupDTO = optionalCupDTO.get();
            String path = userId + "/" + cupDTO.getId() + "/";
            boolean fileUploaded = this.minioService.uploadFile(path, file);
            this.chatService.createCupChat(cupDTO);
            if(fileUploaded){
                return ResponseEntity.ok(cupDTO);
            } else {
                System.out.println("Error uploading file");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(value = "/cups/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<CupDTO> optionalCupDTO = this.cupService.findCupById(id);
        if (optionalCupDTO.isPresent()){
            CupDTO cupDTO = optionalCupDTO.get();
            String path = cupDTO.getUser().getId() + "/" + cupDTO.getId() + "/" + cupDTO.getImage();
            if (this.minioService.deletePathAndFile(path)) {
                this.chatService.deleteCupChat(id);
                this.categoryService.deleteCupFromCategory(id);
                this.cupService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204 response
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(value = "/cups")
    public ResponseEntity<CupDTO> update(@RequestParam("file") Optional<MultipartFile> file, @RequestParam String cup) {
        if(file.isPresent()){
            return ResponseEntity.ok(this.cupService.update(cup, file.get()).get());
        } else {
            return ResponseEntity.ok(this.cupService.update(cup, null).get());
        }

    }
}
