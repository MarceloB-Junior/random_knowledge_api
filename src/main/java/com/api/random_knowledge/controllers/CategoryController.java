package com.api.random_knowledge.controllers;

import com.api.random_knowledge.dtos.CategoryDto;
import com.api.random_knowledge.dtos.responses.ExceptionsResponse;
import com.api.random_knowledge.exceptions.CategoryAlreadyExistsException;
import com.api.random_knowledge.exceptions.CategoryNotFoundException;
import com.api.random_knowledge.models.CategoryModel;
import com.api.random_knowledge.models.CuriosityModel;
import com.api.random_knowledge.services.CategoryService;
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

import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Create a new category",
            description = "Adds a new category for the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully"),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Category name already exists",
                            content = @Content(schema = @Schema(implementation = ExceptionsResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CategoryModel> saveCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Received request to save category: {}", categoryDto.name());
        if(categoryService.existsByName(categoryDto.name())){
            log.warn("Attempt to save category failed: Category already exists - {}",
                    categoryDto.name());
            throw new CategoryAlreadyExistsException("The name of this category is already in use.");
        }
        var categoryModel = new CategoryModel();
        BeanUtils.copyProperties(categoryDto,categoryModel);
        return new ResponseEntity<>(categoryService.save(categoryModel), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get all categories with pagination",
            description = "Retrieves a paginated list of all categories",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved categories",
            content = @Content(schema = @Schema(implementation = PagedModel.class))
    )
    @GetMapping
    public ResponseEntity<Page<CategoryModel>> getAllCategories(@PageableDefault(sort = "categoryId", direction = Sort.Direction.ASC)
                                                  Pageable pageable){
        log.info("Received request to fetch all categories with pagination: {}", pageable);
        return ResponseEntity.ok(categoryService.findAll(pageable));
    }


    @Operation(
            summary = "Get a category by ID",
            description = "Retrieves a specific category by its ID",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(schema = @Schema(implementation = ExceptionsResponse.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryModel> getOneCategory(@PathVariable(value = "id") UUID id){
        log.info("Received request to fetch category with ID: {}", id);
        var categoryModel = categoryService.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        return ResponseEntity.ok(categoryModel);
    }


    @Operation(
            summary = "Get all curiosities for a category",
            description = "Retrieves a paginated list of all curiosities associated with a specific category"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved curiosities",
                            content = @Content(schema = @Schema(implementation = PagedModel.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(schema = @Schema(implementation = ExceptionsResponse.class)))
            }
    )
    @GetMapping("/{id}/curiosities")
    public ResponseEntity<Page<CuriosityModel>> getAllCategoryCuriosities(@PathVariable(value = "id")UUID id,
                                                                          @PageableDefault(sort = "curiosityId", direction = Sort.Direction.ASC)
                                                                          Pageable pageable ){
        log.info("Received request to fetch curiosities for category with ID: {}", id);
        var categoryModel = categoryService.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        return ResponseEntity.ok(categoryService.findCuriositiesByCategory(categoryModel,pageable));
    }


    @Operation(
            summary = "Update an existing category",
            description = "Updates an existing category in the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found",
                            content = @Content(schema = @Schema(implementation = ExceptionsResponse.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Category name already exists",
                            content = @Content(schema = @Schema(implementation = ExceptionsResponse.class))),
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryModel> updateCategory(@PathVariable(value = "id") UUID id,
                                                        @RequestBody @Valid CategoryDto categoryDto){
        log.info("Received request to update category with ID: {}", id);
        var categoryModel = categoryService.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        if(categoryService.existsByName(categoryDto.name()) && !categoryModel.getName().equals(categoryDto.name())){
            log.warn("Attempt to update failed: Category name already exists - {}",
                    categoryDto.name());
            throw new CategoryAlreadyExistsException("The name of this category is already in use.");
        }
        BeanUtils.copyProperties(categoryDto,categoryModel);
        categoryModel.setCategoryId(categoryModel.getCategoryId());
        return ResponseEntity.ok(categoryService.save(categoryModel));
    }


    @Operation(
            summary = "Delete a category",
            description = "Deletes a category from the Random Knowledge API",
            security = @SecurityRequirement(name = "RandomKnowledgeSecurityScheme")
    )
    @ApiResponses(
            value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ExceptionsResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id") UUID id){
        log.info("Received request to delete category with ID: {}", id);
        var categoryModel = categoryService.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        categoryService.delete(categoryModel);
        return ResponseEntity.noContent().build();
    }
}