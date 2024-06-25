package com.sundaegukbap.banchango.feature.recipe.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sundaegukbap.banchango.Recipe
import com.sundaegukbap.banchango.RecipeDifficulty
import com.sundaegukbap.banchango.core.designsystem.component.NetworkImage
import com.sundaegukbap.banchango.core.designsystem.theme.LightOrange
import com.sundaegukbap.banchango.core.designsystem.theme.Orange

@Composable
fun RecipeDetailRoute(
    recipeId: Long,
    modifier: Modifier = Modifier,
    onChangeSystemBarsColor: (color: Color, darkIcons: Boolean) -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getRecipeDetail(recipeId)
    }

    RecipeDetailContent(
        uiState = uiState,
        onChangeSystemBarsColor = onChangeSystemBarsColor,
        modifier = modifier,
    )
}

@Composable
fun RecipeDetailContent(
    uiState: RecipeDetailUiState,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit = {},
    onChangeSystemBarsColor: (color: Color, darkIcons: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is RecipeDetailUiState.Loading -> {
            RecipeDetailLoading()
        }

        is RecipeDetailUiState.Success -> {
            onChangeSystemBarsColor(Color.Transparent, false)
            RecipeDetailScreen(recipe = uiState.recipe, modifier = modifier)
        }

        is RecipeDetailUiState.Error -> {
            onShowErrorSnackBar(uiState.throwable)
        }
    }
}

@Composable
private fun RecipeDetailLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeDetailScreen(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded)
    )

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NetworkImage(
            url = recipe.image,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.5f)
                .align(Alignment.TopCenter),
        )
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                RecipeDetailCard(
                    recipe = recipe,
                    modifier = Modifier
                )
            },
            sheetContainerColor = Color.White,
            sheetPeekHeight = screenHeight * 0.7f,
        ) {}
        val context = LocalContext.current
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            colors = ButtonColors(
                containerColor = Orange,
                contentColor = Color.White,
                disabledContainerColor = LightOrange,
                disabledContentColor = Color.White
            ),
            onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.link)))
            },
        ) {
            Text(text = "레시피 이동하기")
        }
    }
}

@Composable
private fun RecipeDetailCard(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        Column(
            Modifier.padding(horizontal = 16.dp)
        ) {
            RecipeDetailInfo(
                difficulty = recipe.difficulty,
                serving = recipe.servings,
                cookingTime = recipe.cookingTime,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                paddingHorizontal = 32,
            )

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = recipe.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = recipe.introduction,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 12.dp)
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "식재료",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "${recipe.have.size}/${recipe.have.size + recipe.need.size}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(color = Orange, shape = CircleShape)
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterEnd)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(top = 12.dp), color = Orange)
            LazyRow(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(recipe.have) {
                    Box(
                        modifier = Modifier
                            .border(1.dp, color = Orange, shape = CircleShape)
                            .background(color = Color.White, shape = CircleShape)
                    ) {
                        Text(modifier = Modifier.padding(16.dp), text = it)
                    }
                }
                items(recipe.need) {
                    Box(
                        modifier = Modifier
                            .border(1.dp, color = Orange, shape = CircleShape)
                            .background(color = LightOrange, shape = CircleShape)
                    ) {
                        Text(modifier = Modifier.padding(16.dp), text = it)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailCardPreview() {
    RecipeDetailCard(
        recipe = Recipe(
            id = 1,
            name = "간장계란볶음밥",
            introduction = "아주 간단하면서 맛있는 계란간장볶음밥으로 한끼식사 만들어보세요. 아이들이 더 좋아할거예요.",
            image = "https://recipe1.ezmember.co.kr/cache/recipe/2018/05/26/d0c6701bc673ac5c18183b47212a58571.jpg",
            link = "https://www.10000recipe.com/recipe/6889616",
            cookingTime = 10,
            servings = 2,
            difficulty = RecipeDifficulty.BEGINNER,
            have = listOf("계란", "간장"),
            need = listOf("식초", "당근", "감자"),
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    RecipeDetailScreen(
        recipe = Recipe(
            id = 1,
            name = "간장계란볶음밥",
            introduction = "아주 간단하면서 맛있는 계란간장볶음밥으로 한끼식사 만들어보세요. 아이들이 더 좋아할거예요.",
            image = "https://recipe1.ezmember.co.kr/cache/recipe/2018/05/26/d0c6701bc673ac5c18183b47212a58571.jpg",
            link = "https://www.10000recipe.com/recipe/6889616",
            cookingTime = 10,
            servings = 2,
            difficulty = RecipeDifficulty.BEGINNER,
            have = listOf("계란", "간장"),
            need = listOf("식초", "당근", "감자"),
        ),
    )
}
