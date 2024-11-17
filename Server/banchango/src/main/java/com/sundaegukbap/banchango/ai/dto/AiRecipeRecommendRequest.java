package com.sundaegukbap.banchango.ai.dto;

import com.sundaegukbap.banchango.ingredient.domain.Ingredient;

import com.sundaegukbap.banchango.recipe.domain.RecipeCategory;
import java.util.List;
import java.util.stream.Collectors;

public record AiRecipeRecommendRequest(
        List<Long> ingredients,
        int topcategory,
        String subcategory
) {
    public static AiRecipeRecommendRequest of(RecipeCategory recipeCategory, List<Ingredient> ingredients) {
        List<Long> ingredientIds = ingredients.stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());

        return new AiRecipeRecommendRequest(ingredientIds, recipeCategory.getTopCategory(), recipeCategory.getSubCategory());
    }
}
