package com.sundaegukbap.banchango.recipe.application;

import com.sundaegukbap.banchango.ingredient.application.IngredientMatcher;
import com.sundaegukbap.banchango.ingredient.domain.Ingredient;
import com.sundaegukbap.banchango.ingredient.dto.dto.IngredientDto;
import com.sundaegukbap.banchango.ingredient.dto.dto.IngredientDtos;
import com.sundaegukbap.banchango.recipe.domain.Recipe;
import com.sundaegukbap.banchango.recipe.domain.UserRecommendedRecipe;
import com.sundaegukbap.banchango.recipe.dto.response.RecommendedRecipeResponse;
import com.sundaegukbap.banchango.recipe.dto.response.RecommendedRecipeResponses;
import com.sundaegukbap.banchango.recipe.repository.RecipeRepository;
import com.sundaegukbap.banchango.recipe.repository.RecommendedRecipeRepository;
import com.sundaegukbap.banchango.user.domain.User;
import com.sundaegukbap.banchango.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeQueryService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecommendedRecipeRepository recommendedRecipeRepository;
    private final IngredientMatcher ingredientMatcher;

    @Transactional
    public RecommendedRecipeResponse getRecipeDetail(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("no user"));
        Recipe recipe = recipeRepository.findById(recipeId)
            .orElseThrow(() -> new NoSuchElementException("no recipe"));

        return resolveRecipeWithUser(user, recipe);
    }

    @Transactional
    public RecommendedRecipeResponses getRecommendedRecipes(int pageIndex, int pageSize,
        Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("no user"));

        int firstDataIndex = pageIndex * pageSize;
        List<Recipe> recipes;
        if (firstDataIndex >= 50) {
            recipes = recipeRepository.findRecipesByRandom(pageSize);
        } else {
            PageRequest pageRequest = PageRequest.of(pageIndex, pageSize);
            List<UserRecommendedRecipe> userRecommendedRecipeList = recommendedRecipeRepository.findAllByUser(
                pageRequest, user).getContent();
            recipes = userRecommendedRecipeList.stream()
                .map(r -> r.getRecipe())
                .collect(Collectors.toList());
        }

        List<RecommendedRecipeResponse> recommendedRecipeResponseList = recipes.stream()
            .map(recipe -> resolveRecipeWithUser(user, recipe))
            .collect(Collectors.toList());

        return RecommendedRecipeResponses.of(recommendedRecipeResponseList);
    }

    public RecommendedRecipeResponse resolveRecipeWithUser(User user, Recipe recipe) {
        HashMap<String, List> ingredientRelation = ingredientMatcher.checkIngredientRelation(user,
            recipe);
        List<Ingredient> have = ingredientRelation.get("have");
        List<Ingredient> need = ingredientRelation.get("need");

        IngredientDtos haveDtos = IngredientDtos.of(have.stream()
            .map(IngredientDto::of)
            .toList());

        IngredientDtos needDtos = IngredientDtos.of(need.stream()
            .map(IngredientDto::of)
            .toList());

        ingredientMatcher.resolveMainIngredient(recipe, haveDtos);
        ingredientMatcher.resolveMainIngredient(recipe, needDtos);

        return RecommendedRecipeResponse.of(recipe, haveDtos, needDtos);
    }
}
