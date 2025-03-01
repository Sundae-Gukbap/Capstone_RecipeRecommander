package com.sundaegukbap.banchango.recipe.dto.response;

import java.util.List;

public record RecommendedRecipeResponses(
        List<RecommendedRecipeResponse> recommendedRecipeRespons) {
    public static RecommendedRecipeResponses of(List<RecommendedRecipeResponse> recommendedRecipeResponseList){
        return new RecommendedRecipeResponses(recommendedRecipeResponseList);
    }
}
