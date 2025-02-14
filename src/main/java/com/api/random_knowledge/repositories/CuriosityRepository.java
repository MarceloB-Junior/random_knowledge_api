package com.api.random_knowledge.repositories;

import com.api.random_knowledge.models.CategoryModel;
import com.api.random_knowledge.models.CuriosityModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CuriosityRepository extends JpaRepository<CuriosityModel, UUID> {
    boolean existsByCuriosityAndCategory(String curiosity, CategoryModel category);
    Page<CuriosityModel> findAllByCategory(CategoryModel category, Pageable pageable);

    @Query("SELECT c FROM CuriosityModel c ORDER BY FUNCTION('RAND') LIMIT 1")
    Optional<CuriosityModel> findRandom();
}
