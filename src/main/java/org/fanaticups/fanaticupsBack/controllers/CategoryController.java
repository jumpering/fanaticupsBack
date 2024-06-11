package org.fanaticups.fanaticupsBack.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fanaticups.fanaticupsBack.dao.entities.CategoryEntity;
import org.fanaticups.fanaticupsBack.models.CategoryDTO;
import org.fanaticups.fanaticupsBack.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found categories",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryEntity.class))}
            )})
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/categories")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categoryDTOList = this.categoryService.findAll();
        return categoryDTOList.isEmpty()? ResponseEntity.noContent().build():ResponseEntity.ok(categoryDTOList);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/categories")
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid CategoryDTO categoryDTO) {
        Optional<CategoryDTO> categoryDTOOptional = this.categoryService.save(categoryDTO);
        return categoryDTOOptional.isPresent() ? ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO) : ResponseEntity.notFound().build();
    }
}
