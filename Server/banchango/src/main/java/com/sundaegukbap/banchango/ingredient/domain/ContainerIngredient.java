package com.sundaegukbap.banchango.ingredient.domain;

import com.sundaegukbap.banchango.container.domain.Container;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="container_ingredients")
@EntityListeners(AuditingEntityListener.class)
public class ContainerIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="container_id")
    private Container container;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ingredient_id")
    private Ingredient ingredient;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime expriationDate;

    @Builder
    public ContainerIngredient(Long id, Container container, Ingredient ingredient, LocalDateTime createdAt, LocalDateTime expriationDate) {
        this.id = id;
        this.container = container;
        this.ingredient = ingredient;
        this.createdAt = createdAt;
        this.expriationDate = expriationDate;
    }
}
