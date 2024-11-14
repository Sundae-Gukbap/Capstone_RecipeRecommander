package com.sundaegukbap.banchango.ingredient.dto.dto;

import com.sundaegukbap.banchango.ingredient.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IngredientDto {

    private Long id;
    private String name;
    private String kind;
    private String image;
    private boolean isMain;

    public static IngredientDto of(Ingredient ingredient) {
        return new IngredientDto(
            ingredient.getId(),
            ingredient.getName(),
            ingredient.getKind(),
            ingredient.getImage(),
            false
        );
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }
}
