package com.sundaegukbap.banchango.core.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NeedResponse(
    @SerialName("ingredientDtos")
    val ingredients: List<IngredientResponse>,
)
