package com.api.random_knowledge.services;

import com.api.random_knowledge.models.CategoryModel;
import com.api.random_knowledge.models.CuriosityModel;
import com.api.random_knowledge.repositories.CuriosityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuriosityServiceTest {

    @Mock
    private CuriosityRepository curiosityRepository;

    @InjectMocks
    private CuriosityService curiosityService;

    @Test
    public void saveCuriosityReturnsCuriosityModel() {
        CuriosityModel curiosityModel = new CuriosityModel();
        curiosityModel.setCuriosity("Did you know that honey never spoils?");

        when(curiosityRepository.save(any(CuriosityModel.class))).thenReturn(curiosityModel);
        CuriosityModel savedCuriosity = curiosityService.save(curiosityModel);

        assertNotNull(savedCuriosity);
        assertEquals("Did you know that honey never spoils?", savedCuriosity.getCuriosity());
        verify(curiosityRepository, times(1)).save(curiosityModel);
    }

    @Test
    public void findAllReturnsPageOfCuriosities() {
        Pageable pageable = Pageable.ofSize(10);
        var curiosities = List.of(new CuriosityModel(), new CuriosityModel());
        Page<CuriosityModel> page = new PageImpl<>(curiosities);

        when(curiosityRepository.findAll(pageable)).thenReturn(page);
        Page<CuriosityModel> resultPage = curiosityService.findAll(pageable);

        assertNotNull(resultPage);
        assertEquals(curiosities.size(), resultPage.getContent().size());
        verify(curiosityRepository, times(1)).findAll(pageable);
    }

    @Test
    public void findByIdReturnsCuriosityModelOptional() {
        UUID id = UUID.randomUUID();
        CuriosityModel curiosityModel = new CuriosityModel();
        curiosityModel.setCuriosityId(id);

        when(curiosityRepository.findById(id)).thenReturn(Optional.of(curiosityModel));
        Optional<CuriosityModel> resultOptional = curiosityService.findById(id);

        assertTrue(resultOptional.isPresent());
        assertEquals(id, resultOptional.get().getCuriosityId());
        verify(curiosityRepository, times(1)).findById(id);
    }

    @Test
    public void findByIdReturnsEmptyOptional() {
        UUID id = UUID.randomUUID();

        when(curiosityRepository.findById(id)).thenReturn(Optional.empty());
        Optional<CuriosityModel> resultOptional = curiosityService.findById(id);

        assertTrue(resultOptional.isEmpty());
        verify(curiosityRepository, times(1)).findById(id);
    }

    @Test
    public void findRandomReturnsCuriosityOptional() {
        CuriosityModel curiosityModel = new CuriosityModel();
        curiosityModel.setCuriosity("Did you know that octopuses have three hearts?");

        when(curiosityRepository.findRandom()).thenReturn(Optional.of(curiosityModel));
        Optional<CuriosityModel> resultOptional = curiosityService.findRandom();

        assertTrue(resultOptional.isPresent());
        assertEquals("Did you know that octopuses have three hearts?", resultOptional.get().getCuriosity());
        verify(curiosityRepository, times(1)).findRandom();
    }

    @Test
    public void existsByCuriosityAndCategoryReturnsTrue() {
        String curiosity = "Did you know that bananas are berries?";
        CategoryModel categoryModel = new CategoryModel();

        when(curiosityRepository.existsByCuriosityAndCategory(anyString(), any(CategoryModel.class)))
                .thenReturn(true);
        boolean exists = curiosityService.existsByCuriosityAndCategory(curiosity, categoryModel);

        assertTrue(exists);
        verify(curiosityRepository, times(1))
                .existsByCuriosityAndCategory(curiosity, categoryModel);
    }

    @Test
    public void existsByCuriosityAndCategoryReturnsFalse() {
        String curiosity = "Did you know that bananas are berries?";
        CategoryModel categoryModel = new CategoryModel();

        when(curiosityRepository.existsByCuriosityAndCategory(anyString(), any(CategoryModel.class)))
                .thenReturn(false);
        boolean exists = curiosityService.existsByCuriosityAndCategory(curiosity, categoryModel);

        assertFalse(exists);
        verify(curiosityRepository, times(1))
                .existsByCuriosityAndCategory(curiosity, categoryModel);
    }

    @Test
    public void deleteRemovesCuriosity() {
        CuriosityModel curiosityModel = new CuriosityModel();

        doNothing().when(curiosityRepository).delete(any(CuriosityModel.class));
        curiosityService.delete(curiosityModel);

        verify(curiosityRepository, times(1)).delete(curiosityModel);
    }
}
