package com.sundaegukbap.banchango.ai.application;

import com.sundaegukbap.banchango.ai.dto.AiRecipeRecommendRequest;
import com.sundaegukbap.banchango.ai.dto.AiRecipeRecommendResponse;
import com.sundaegukbap.banchango.ingredient.domain.Ingredient;
import com.sundaegukbap.banchango.recipe.domain.RecipeCategory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AiRecipeRecommendClient {
    @Value("${api.aiBaseUrl}")
    private String aiBaseUrl;
    private final RestTemplate restTemplate;

    public AiRecipeRecommendClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Transactional
    public List<Long> getRecommendedRecipesFromAI(RecipeCategory category, List<Ingredient> ingredientList) {
        AiRecipeRecommendRequest request = AiRecipeRecommendRequest.of(category, ingredientList);
        AiRecipeRecommendResponse response = restTemplate.postForObject(aiBaseUrl + "/category_recommend", request, AiRecipeRecommendResponse.class);

        return response.recommended_recipes();
    }
}
