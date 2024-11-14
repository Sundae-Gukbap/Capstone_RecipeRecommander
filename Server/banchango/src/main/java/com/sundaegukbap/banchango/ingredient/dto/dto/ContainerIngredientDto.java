package com.sundaegukbap.banchango.ingredient.dto.dto;

import com.sundaegukbap.banchango.container.dto.dto.ContainerDto;
import com.sundaegukbap.banchango.ingredient.domain.ContainerIngredient;
import java.time.LocalDateTime;

public record ContainerIngredientDto(
        Long containerIngredientId,
        ContainerDto containerDto,
        IngredientDto ingredientDto,
        LocalDateTime createdAt,
        LocalDateTime expirationDate
) {
    public static ContainerIngredientDto of(ContainerIngredient containerIngredient){
        return new ContainerIngredientDto(
                containerIngredient.getId(),
                ContainerDto.of(containerIngredient.getContainer()),
                IngredientDto.of(containerIngredient.getIngredient()),
                containerIngredient.getCreatedAt(),
                containerIngredient.getExpriationDate());
    }
}
