package com.api.random_knowledge.services;

import com.api.random_knowledge.models.CategoryModel;
import com.api.random_knowledge.models.CuriosityModel;
import com.api.random_knowledge.repositories.CategoryRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CuriosityRepository curiosityRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void saveCategoryReturnsCategoryModel() {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName("Science");

        when(categoryRepository.save(any(CategoryModel.class))).thenReturn(categoryModel);

        CategoryModel savedCategory = categoryService.save(categoryModel);

        assertNotNull(savedCategory);
        assertEquals("Science", savedCategory.getName());
        verify(categoryRepository, times(1)).save(categoryModel);
    }

    @Test
    public void findAllReturnsPageOfCategories() {
        Pageable pageable = Pageable.ofSize(10);
        var categories = List.of(new CategoryModel(), new CategoryModel());
        Page<CategoryModel> page = new PageImpl<>(categories);

        when(categoryRepository.findAll(pageable)).thenReturn(page);
        Page<CategoryModel> resultPage = categoryService.findAll(pageable);

        assertEquals(categories.size(), resultPage.getContent().size());
        verify(categoryRepository, times(1)).findAll(pageable);
    }

    @Test
    public void findByIdReturnsCategoryModelOptional() {
        UUID id = UUID.randomUUID();
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCategoryId(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(categoryModel));
        Optional<CategoryModel> resultOptional = categoryService.findById(id);

        assertTrue(resultOptional.isPresent());
        assertEquals(id, resultOptional.get().getCategoryId());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    public void findByIdReturnsEmptyOptional() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        Optional<CategoryModel> resultOptional = categoryService.findById(id);

        assertTrue(resultOptional.isEmpty());
        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    public void findCuriositiesByCategoryReturnsPageOfCuriosities() {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName("Science");

        Pageable pageable = Pageable.ofSize(10);
        var curiosities = List.of(new CuriosityModel(), new CuriosityModel());
        Page<CuriosityModel> page = new PageImpl<>(curiosities);

        when(curiosityRepository.findAllByCategory(categoryModel, pageable)).thenReturn(page);
        Page<CuriosityModel> resultPage = categoryService.findCuriositiesByCategory(categoryModel, pageable);

        assertNotNull(resultPage);
        assertEquals(curiosities.size(), resultPage.getContent().size());
        verify(curiosityRepository, times(1)).findAllByCategory(categoryModel, pageable);
    }

    @Test
    public void existsByNameReturnsTrue() {
        String name = "Science";

        when(categoryRepository.existsByName(name)).thenReturn(true);
        boolean exists = categoryService.existsByName(name);

        assertTrue(exists);
        verify(categoryRepository, times(1)).existsByName(name);
    }

    @Test
    public void existsByNameReturnsFalse() {
        String name = "Science";

        when(categoryRepository.existsByName(name)).thenReturn(false);
        boolean exists = categoryService.existsByName(name);

        assertFalse(exists);
        verify(categoryRepository, times(1)).existsByName(name);
    }

    @Test
    public void deleteRemovesCategory() {
        CategoryModel categoryModel = new CategoryModel();

        doNothing().when(categoryRepository).delete(categoryModel);
        categoryService.delete(categoryModel);

        verify(categoryRepository, times(1)).delete(categoryModel);
    }
}
