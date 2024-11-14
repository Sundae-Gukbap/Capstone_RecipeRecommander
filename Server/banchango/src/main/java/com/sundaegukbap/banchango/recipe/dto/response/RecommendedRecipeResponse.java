package com.sundaegukbap.banchango.recipe.dto.response;

import com.sundaegukbap.banchango.ingredient.dto.dto.IngredientDtos;
import com.sundaegukbap.banchango.recipe.domain.Recipe;
import com.sundaegukbap.banchango.recipe.dto.dto.RecipeDto;

public record RecommendedRecipeResponse(
    RecipeDto recipe,
    IngredientDtos have,
    IngredientDtos need) {

    public static RecommendedRecipeResponse of(Recipe recipe, IngredientDtos have,
        IngredientDtos need) {
        return new RecommendedRecipeResponse(
            RecipeDto.of(recipe),
            have,
            need
        );
    }
}