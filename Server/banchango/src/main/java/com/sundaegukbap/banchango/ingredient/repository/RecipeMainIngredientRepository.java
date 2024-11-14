package com.sundaegukbap.banchango.ingredient.repository;

import com.sundaegukbap.banchango.ingredient.domain.RecipeMainIngredient;
import com.sundaegukbap.banchango.recipe.domain.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeMainIngredientRepository extends JpaRepository<RecipeMainIngredient, Long> {

    List<RecipeMainIngredient> findByRecipe(Recipe recipe);

}
