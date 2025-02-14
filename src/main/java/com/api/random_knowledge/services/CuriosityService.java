package com.api.random_knowledge.services;

import com.api.random_knowledge.models.CategoryModel;
import com.api.random_knowledge.models.CuriosityModel;
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
public class CuriosityService {

    private final CuriosityRepository curiosityRepository;

    @Transactional
    public CuriosityModel save(CuriosityModel curiosityModel){
        log.debug("Attempting to save curiosity: {}", curiosityModel.getCuriosity());
        return curiosityRepository.save(curiosityModel);
    }

    public Page<CuriosityModel> findAll(Pageable pageable){
        log.debug("Fetching all curiosities with pagination: {}", pageable);
        return curiosityRepository.findAll(pageable);
    }

    public Optional<CuriosityModel> findById(UUID id){
        log.debug("Searching for curiosity with ID: {}", id);
        return curiosityRepository.findById(id);
    }

    public Optional<CuriosityModel> findRandom(){
        log.debug("Fetching a random curiosity");
        return curiosityRepository.findRandom();
    }

    public boolean existsByCuriosityAndCategory(String curiosity, CategoryModel categoryModel){
        log.debug("Checking existence of curiosity '{}' in category '{}'", curiosity,
                categoryModel.getName());
        return curiosityRepository.existsByCuriosityAndCategory(curiosity,categoryModel);
    }

    @Transactional
    public void delete(CuriosityModel curiosityModel){
        log.debug("Attempting to delete curiosity with ID: {}", curiosityModel.getCuriosityId());
        curiosityRepository.delete(curiosityModel);
    }
}
