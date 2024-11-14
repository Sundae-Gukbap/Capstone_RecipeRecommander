package com.sundaegukbap.banchango.ingredient.dto.dto;

import java.util.List;

public record IngredientDtos(
        List<IngredientDto> ingredientDtos
) {
    public static IngredientDtos of(List<IngredientDto> ingredientDtos){
        return new IngredientDtos(ingredientDtos);
    }
}
