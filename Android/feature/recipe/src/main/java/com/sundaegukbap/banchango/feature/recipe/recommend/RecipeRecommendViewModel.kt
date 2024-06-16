package com.sundaegukbap.banchango.feature.recipe.recommend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundaegukbap.banchango.Recipe
import com.sundaegukbap.banchango.core.data.repository.api.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeRecommendViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<RecipeRecommendUiState> =
        MutableStateFlow(RecipeRecommendUiState.Loading)
    val uiState: StateFlow<RecipeRecommendUiState> = _uiState.asStateFlow()


    fun getRecipeRecommendation() {
        val remainRecipes = (_uiState.value as? RecipeRecommendUiState.Success)?.recipes ?: listOf()

        viewModelScope.launch {
            recipeRepository.getRecipeRecommendation()
                .onSuccess { recipes ->
                    _uiState.value = RecipeRecommendUiState.Success(
                        remainRecipes + recipes.toUiState()
                    )
                }.onFailure { throwable ->
                    throwable.printStackTrace()
                }
        }
    }

    fun likeRecipe(recipe: Recipe) {
        val successUiState = _uiState.value as? RecipeRecommendUiState.Success ?: return
        val likedRecipeUiStates = successUiState.recipes.map { recipeUiState ->
            if (recipeUiState.recipe.id == recipe.id) {
                recipeUiState.copy(isLiked = !recipeUiState.isLiked)
            } else {
                recipeUiState
            }
        }
        _uiState.value = RecipeRecommendUiState.Success(likedRecipeUiStates)
    }

    private fun List<Recipe>.toUiState(): List<RecipeRecommendItemUiState> {
        return map { recipe ->
            RecipeRecommendItemUiState(recipe = recipe, isLiked = false)
        }
    }
}
