package com.api.random_knowledge.services;

import com.api.random_knowledge.models.CategoryModel;
import com.api.random_knowledge.models.CuriosityModel;
import com.api.random_knowledge.repositories.CategoryRepository;
import com.api.random_knowledge.repositories.CuriosityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CuriosityRepository curiosityRepository;

    @Transactional
    public CategoryModel save(CategoryModel categoryModel){
        log.debug("Attempting to save category: {}", categoryModel.getName());
        return categoryRepository.save(categoryModel);
    }

    public Page<CategoryModel> findAll(Pageable pageable){
        log.debug("Fetching all categories with pagination: {}", pageable);
        return categoryRepository.findAll(pageable);
    }

    public Optional<CategoryModel> findById(UUID id){
        log.debug("Searching for category with ID: {}", id);
        return categoryRepository.findById(id);
    }

    public Page<CuriosityModel> findCuriositiesByCategory(CategoryModel categoryModel, Pageable pageable){
        log.debug("Fetching curiosities for category: {} with pagination: {}", categoryModel.getName(),
                pageable);
        return curiosityRepository.findAllByCategory(categoryModel, pageable);
    }

    public boolean existsByName(String name){
        log.debug("Checking existence of category with name: {}", name);
        return categoryRepository.existsByName(name);
    }

    @Transactional
    public void delete(CategoryModel categoryModel){
        log.debug("Attempting to delete category with ID: {}", categoryModel.getCategoryId());
        categoryRepository.delete(categoryModel);
    }
}
