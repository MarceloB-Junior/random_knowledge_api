package com.api.random_knowledge.controllers;

import com.api.random_knowledge.dtos.CuriosityDto;
import com.api.random_knowledge.dtos.responses.ExceptionsResponse;
import com.api.random_knowledge.exceptions.CategoryNotFoundException;
import com.api.random_knowledge.exceptions.CuriosityAlreadyExistsException;
import com.api.random_knowledge.exceptions.CuriosityNotFoundException;
import com.api.random_knowledge.models.CuriosityModel;
import com.api.random_knowledge.services.CategoryService;
import com.api.random_knowledge.services.CuriosityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/curiosities")
public class CuriosityController {

    private final CuriosityService curiosityService;
    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new curiosity for a category",
            description = "Adds a new curiosity to a specific category in the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "201", description = "Curiosity created successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ExceptionsResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Curiosity already exists in the specified category",
                    content = @Content(schema = @Schema(implementation = ExceptionsResponse.class)))
            }
    )
    @PostMapping
    public ResponseEntity<CuriosityModel> saveCuriosity(@RequestParam(value = "category") UUID id,
                                                        @RequestBody @Valid CuriosityDto curiosityDto){
        log.info("Received request to save curiosity: {} for category ID: {}", curiosityDto.curiosity(), id);
        var categoryModel = categoryService.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        if(curiosityService.existsByCuriosityAndCategory(curiosityDto.curiosity(), categoryModel)){
            log.warn("Attempt to save curiosity failed: Curiosity already exists in the specified category - {}"
                    , curiosityDto.curiosity());
            throw new CuriosityAlreadyExistsException("Curiosity already exists in the specified category.");
        }
        var curiosityModel = new CuriosityModel();
        BeanUtils.copyProperties(curiosityDto,curiosityModel);
        curiosityModel.setCategory(categoryModel);
        curiosityModel.setCreatedAt(LocalDateTime.now());
        return new ResponseEntity<>(curiosityService.save(curiosityModel), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get all curiosities with pagination",
            description = "Retrieves a paginated list of all curiosities in the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved curiosities",
            content = @Content(schema = @Schema(implementation = PagedModel.class))
    )
    @GetMapping
    public ResponseEntity<Page<CuriosityModel>> getAllCuriosities(@PageableDefault(sort = "curiosityId", direction = Sort.Direction.ASC)
                                                                      Pageable pageable){
        log.info("Received request to fetch all curiosities with pagination: {}", pageable);
        return ResponseEntity.ok(curiosityService.findAll(pageable));
    }


    @Operation(
            summary = "Get a curiosity by ID",
            description = "Retrieves a specific curiosity by its ID from the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved curiosity"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curiosity not found",
                    content = @Content(schema = @Schema(implementation = ExceptionsResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CuriosityModel> getOneCuriosity(@PathVariable(value = "id") UUID id){
        log.info("Received request to fetch curiosity with ID: {}", id);
        var curiosityModel = curiosityService.findById(id)
                .orElseThrow(() -> new CuriosityNotFoundException("Curiosity not found."));
        return ResponseEntity.ok(curiosityModel);
    }


    @Operation(
            summary = "Get a random curiosity",
            description = "Retrieves a random curiosity from the Random Knowledge API"
    )
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved random curiosity"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No curiosity found",
                    content = @Content(schema = @Schema(implementation = ExceptionsResponse.class)))
            }
    )
    @GetMapping("/random")
    public ResponseEntity<CuriosityModel> getRandomCuriosity(){
        log.info("Received request to fetch a random curiosity");
        var curiosityModel = curiosityService.findRandom()
                .orElseThrow(() -> new CuriosityNotFoundException("No curiosity found."));
        return ResponseEntity.ok(curiosityModel);
    }


    @Operation(
            summary = "Update an existing curiosity",
            description = "Updates an existing curiosity in the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "200", description = "Curiosity updated successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curiosity not found",
                    content = @Content(schema = @Schema(implementation = ExceptionsResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Curiosity already exists in the specified category",
                    content = @Content(schema = @Schema(implementation = ExceptionsResponse.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CuriosityModel> updateCuriosity(@PathVariable(value = "id")UUID id,
                                                          @RequestBody @Valid CuriosityDto curiosityDto){
        log.info("Received request to update curiosity with ID: {}", id);
        var curiosityModel = curiosityService.findById(id)
                .orElseThrow(() -> new CuriosityNotFoundException("Curiosity not found."));
        if(curiosityService.existsByCuriosityAndCategory(curiosityDto.curiosity(),
                curiosityModel.getCategory()) && !curiosityModel.getCuriosity().equals(curiosityDto.curiosity())){
            log.warn("Attempt to update failed: Curiosity already exists in the specified category - {}",
                    curiosityDto.curiosity());
            throw new CuriosityAlreadyExistsException("Curiosity already exists in the specified category.");
        }
        BeanUtils.copyProperties(curiosityDto,curiosityModel);
        return ResponseEntity.ok(curiosityService.save(curiosityModel));
    }


    @Operation(
            summary = "Delete a curiosity",
            description = "Deletes a curiosity from the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Curiosity deleted successfully"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Curiosity not found",
                            content = @Content(schema = @Schema(implementation = ExceptionsResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuriosity(@PathVariable(value = "id")UUID id){
        log.info("Received request to delete curiosity with ID: {}", id);
        var curiosityModel = curiosityService.findById(id)
                .orElseThrow(() -> new CuriosityNotFoundException("Curiosity not found."));
        curiosityModel.getCategory().getCuriosities().remove(curiosityModel);
        curiosityService.delete(curiosityModel);
        return ResponseEntity.noContent().build();
    }

}