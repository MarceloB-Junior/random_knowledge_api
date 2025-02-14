package com.api.random_knowledge.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "TB_CURIOSITY")
public class CuriosityModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(value = "curiosity_id", index = 1)
    private UUID curiosityId;
    @Column(columnDefinition = "TEXT")
    private String curiosity;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    private CategoryModel category;
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;
}