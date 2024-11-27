package com.sundaegukbap.banchango.recipe.presentation;

import com.sundaegukbap.banchango.recipe.application.RecipeQueryService;
import com.sundaegukbap.banchango.recipe.application.RecipeService;
import com.sundaegukbap.banchango.recipe.domain.RecipeCategory;
import com.sundaegukbap.banchango.recipe.dto.response.RecommendedRecipeResponse;
import com.sundaegukbap.banchango.recipe.dto.response.RecommendedRecipeResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipe")
@AllArgsConstructor
@Tag(name = "레시피 조회 컨트롤러")
public class RecipeController {

    private final RecipeQueryService recipeQueryService;
    private final RecipeService recipeService;

    @GetMapping("/recommend/{userId}")
    @Operation(summary = "추천 레시피 목록 조회", description = "추천 레시피 목록을 조회합니다.")
    public ResponseEntity<RecommendedRecipeResponses> getRecommendedRecipes(
        @PathVariable("userId") Long userId,
        @RequestParam(defaultValue = "0") int pageIndex,
        @RequestParam(defaultValue = "10") int pageSize) {
        RecommendedRecipeResponses response = recipeQueryService.getRecommendedRecipes(pageIndex,
            pageSize, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}/{recipeId}")
    @Operation(summary = "특정 레시피 상세 조회", description = "레시피를 조회한다.")
    public ResponseEntity<RecommendedRecipeResponse> getRecipeDetail(
        @PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId) {
        RecommendedRecipeResponse response = recipeQueryService.getRecipeDetail(userId, recipeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    @Operation(summary = "추천 레시피 카테고리 변경", description = "추천 레시피 카테고리를 변경하고 새로운 추천 레시피를 받아옵니다.")
    public ResponseEntity<String> changeRecipeCategory(@PathVariable("userId") Long userId,
        @RequestParam(defaultValue = "전체") RecipeCategory recipeCategory) {
        recipeService.changeRecipeCategory(userId, recipeCategory);
        return new ResponseEntity<>("카테고리에 맞게 추천 레시피목록이 변경되었습니다.", HttpStatus.OK);
    }
}
