package com.sundaegukbap.banchango.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sundaegukbap.banchango.Container
import com.sundaegukbap.banchango.ContainerIngredient
import com.sundaegukbap.banchango.Ingredient
import com.sundaegukbap.banchango.IngredientContainer
import com.sundaegukbap.banchango.IngredientKind
import com.sundaegukbap.banchango.KindIngredientContainer
import com.sundaegukbap.banchango.core.designsystem.theme.BanchangoTheme
import com.sundaegukbap.banchango.core.designsystem.theme.Black
import com.sundaegukbap.banchango.core.designsystem.theme.Gray
import com.sundaegukbap.banchango.core.designsystem.theme.LightOrange
import com.sundaegukbap.banchango.core.designsystem.theme.LightestOrange
import com.sundaegukbap.banchango.core.designsystem.theme.Orange
import com.sundaegukbap.banchango.core.designsystem.theme.White
import com.sundaegukbap.banchango.core.designsystem.theme.lightGray
import com.sundaegukbap.banchango.feature.home.component.AddButton
import com.sundaegukbap.banchango.feature.home.component.IngredientItem
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    padding: PaddingValues,
    onChangeStatusBarColor: (color: Color, darkIcons: Boolean) -> Unit,
    showError: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedContainer by remember { mutableStateOf(Container(-1L, "")) }

    viewModel.collectSideEffect {
        showError(it)
    }
    HomeScreen(
        padding = padding,
        ingredientContainers = state.ingredientContainers,
        onChangeStatusBarColor = onChangeStatusBarColor,
        onContainerAddClicked = viewModel::addContainer,
        onKindContainerClicked = { container, ingredientKind ->
            selectedContainer = container
            viewModel.getKindIngredientContainerDetail(container, ingredientKind)
        },
        onAddIngredientClicked = { showBottomSheet = true }
    )

    if (state.isDetailShowing && state.kindIngredientContainerDetail != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            KindIngredientContainerDetailScreen(
                padding = padding,
                kindIngredientContainer = state.kindIngredientContainerDetail!!,
                onBackClicked = viewModel::closeDetail,
                onAddIngredientClicked = { showBottomSheet = true },
            )
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            containerColor = White,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(0.7f)
        ) {
            Box(Modifier.fillMaxSize()) {
                Column {
                    OutlinedTextField(
                        value = state.ingredientQuery,
                        onValueChange = { query -> viewModel.getAllIngredients(query) },
                        label = { Text("이름으로 재료 검색") },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors().copy(
                            focusedLabelColor = Orange,
                            focusedIndicatorColor = Orange,
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.selectedIngredients.isEmpty()) {
                        Text(
                            "추가할 재료를 선택해주세요",
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center,
                            color = Orange,
                        )
                    } else {
                        LazyRow(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                        ) {
                            item { Spacer(Modifier.width(16.dp)) }
                            items(state.selectedIngredients) {
                                InputChip(
                                    label = { Text(text = it.name) },
                                    onClick = { viewModel.unSelectIngredient(it) },
                                    modifier = Modifier.padding(8.dp),
                                    selected = true,
                                    colors = InputChipDefaults.inputChipColors().copy(
                                        selectedContainerColor = Orange,
                                        selectedLabelColor = White,
                                        selectedTrailingIconColor = White,
                                    ),
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_close),
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    LazyColumn(Modifier.fillMaxHeight()) {
                        items(state.ingredients) {
                            val isSelected = it in state.selectedIngredients
                            val onClick =
                                if (isSelected) viewModel::unSelectIngredient else viewModel::selectIngredient
                            OutlinedButton(
                                onClick = { onClick(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                colors = ButtonDefaults.outlinedButtonColors().copy(
                                    containerColor = if (isSelected) {
                                        LightOrange
                                    } else {
                                        White
                                    },
                                    contentColor = if (isSelected) {
                                        White
                                    } else {
                                        Black
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = it.name,
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                val isNotEmpty = state.selectedIngredients.isNotEmpty()
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = White,
                        contentColor = Orange,
                        disabledContainerColor = lightGray,
                    ),
                    enabled = isNotEmpty,
                    border = if (isNotEmpty) BorderStroke(1.dp, Orange) else
                        BorderStroke(1.dp, lightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        viewModel.addIngredientsToContainer(selectedContainer)
                    }) {
                    Text("총 ${state.selectedIngredients.size}개의 재료 추가하기", Modifier.padding(8.dp))
                }
            }
        }
    }
}


@Composable
private fun KindIngredientContainerDetailScreen(
    padding: PaddingValues,
    kindIngredientContainer: KindIngredientContainer,
    onBackClicked: () -> Unit,
    onAddIngredientClicked: () -> Unit,
) {
    BackHandler(onBack = onBackClicked)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(padding)
            .padding(16.dp),
    ) {
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors().copy(containerColor = LightestOrange),
        ) {
            Row(Modifier.padding(horizontal = 8.dp)) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = kindIngredientContainer.ingredients.first().container.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Orange
                )
                Text(
                    modifier = Modifier
                        .background(color = White, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .align(Alignment.CenterVertically),
                    text = kindIngredientContainer.kind.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(kindIngredientContainer.ingredients) { ingredient ->
                IngredientItem(
                    ingredient = ingredient.ingredient,
                    expirationDate = ingredient.expirationDate,
                    createdAt = ingredient.createdAt
                )
            }
            item {
                AddButton(
                    containerColor = LightOrange,
                    onAddClick = onAddIngredientClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewKindIngredientContainerDetailScreen() {
    KindIngredientContainerDetailScreen(
        PaddingValues(16.dp),
        KindIngredientContainer(
            IngredientKind.VEGETABLE,
            listOf(
                ContainerIngredient(
                    1,
                    Container(1, "냉장 1"),
                    Ingredient(2, "상추", IngredientKind.VEGETABLE, ""),
                    LocalDateTime.now(),
                    LocalDateTime.now()
                ),
                ContainerIngredient(
                    1,
                    Container(1, "냉장 1"),
                    Ingredient(3, "배추", IngredientKind.VEGETABLE, ""),
                    LocalDateTime.now(),
                    LocalDateTime.now()
                )
            )
        ),
        onBackClicked = {},
        onAddIngredientClicked = {}
    )
}

@Composable
private fun HomeScreen(
    padding: PaddingValues,
    ingredientContainers: List<IngredientContainer>,
    onContainerAddClicked: (name: String) -> Unit,
    onKindContainerClicked: (container: Container, kind: IngredientKind) -> Unit,
    onAddIngredientClicked: () -> Unit,
    onChangeStatusBarColor: (color: Color, darkIcons: Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        items(ingredientContainers) { ingredientContainer ->
            val isEven = ingredientContainers.indexOf(ingredientContainer) % 2 == 0
            val containerColor = if (isEven) LightOrange else Gray
            val itemColor = if (isEven) Gray else White
            val buttonColor = if (isEven) White else Gray
            val ingredientContainerNameColor = if (isEven) White else LightOrange

            ElevatedCard(
                colors = CardDefaults.elevatedCardColors().copy(containerColor = containerColor),
                elevation = CardDefaults.elevatedCardElevation(2.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(0.4f),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = ingredientContainerNameColor,
                        text = ingredientContainer.container.name
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
                    Icon(
                        modifier = Modifier
                            .weight(0.1f)
                            .clickable(onClick = onAddIngredientClicked),
                        tint = ingredientContainerNameColor,
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null
                    )
                }
                val kindIngredients = ingredientContainer.kindIngredientContainers
                val totalIngredients = kindIngredients.size
                val onItemClicked: (kind: IngredientKind) -> Unit = { kind ->
                    onKindContainerClicked(ingredientContainer.container, kind)
                }

                for (index in 0 until totalIngredients step 2) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween // Distributes items evenly
                    ) {
                        // First IngredientItem
                        KindIngredientContainerItem(
                            containerColor = itemColor,
                            kindIngredientContainer = kindIngredients[index],
                            buttonColor = buttonColor,
                            onIngredientItemClicked = onItemClicked,
                            modifier = Modifier.weight(0.4f) // Fixed width for consistent size
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        // Check for the second item
                        if (index + 1 < totalIngredients) {
                            KindIngredientContainerItem(
                                kindIngredientContainer = kindIngredients[index + 1],
                                modifier = Modifier.weight(0.4f), // Fixed width for consistent size
                                containerColor = itemColor,
                                onIngredientItemClicked = onItemClicked,
                                buttonColor = buttonColor,
                            )
                        } else {
                            // If the second item does not exist, add a spacer for alignment
                            Spacer(modifier = Modifier.weight(0.4f)) // Takes remaining space
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            AddButton(
                containerColor = if (ingredientContainers.size % 2 == 0) LightOrange else Gray,
                onAddClick = { onContainerAddClicked("냉장 1") },
                modifier = Modifier.height(50.dp),
            )
        }
    }
}


@Composable
private fun KindIngredientContainerItem(
    containerColor: Color,
    buttonColor: Color,
    kindIngredientContainer: KindIngredientContainer,
    onIngredientItemClicked: (kind: IngredientKind) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
            .height(106.dp)
            .clickable(onClick = { onIngredientItemClicked(kindIngredientContainer.kind) })
    ) {
        Column(Modifier.padding(8.dp)) {
            val ingredients = kindIngredientContainer.ingredients
            Row {
                Text(
                    text = kindIngredientContainer.kind.label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(0.3f))
                Text(
                    modifier = Modifier
                        .background(
                            color = buttonColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = ingredients.size.toString() + "개",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Orange
                )
            }
            ingredients.subList(0, minOf(ingredients.size, 2)).forEach { ingredient ->
                val dDay = ChronoUnit.DAYS.between(
                    LocalDateTime.now(),
                    ingredient.expirationDate,
                )
                Row {
                    Text(
                        text = ingredient.ingredient.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "D - $dDay", style = MaterialTheme.typography.bodySmall)
                }
            }
            if (ingredients.size > 2) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "...",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewIngredientItem() {
    BanchangoTheme {
        KindIngredientContainerItem(
            containerColor = White,
            buttonColor = Gray,
            onIngredientItemClicked = { _ -> },
            kindIngredientContainer = KindIngredientContainer(
                IngredientKind.VEGETABLE,
                listOf(
                    ContainerIngredient(
                        1,
                        Container(1, "냉장 1"),
                        Ingredient(1, "상추", IngredientKind.VEGETABLE, ""),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                    ),
                    ContainerIngredient(
                        1,
                        Container(1, "냉장 1"),
                        Ingredient(1, "상추", IngredientKind.VEGETABLE, ""),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    BanchangoTheme {
        HomeScreen(
            padding = PaddingValues(0.dp),
            onChangeStatusBarColor = { _, _ -> },
            ingredientContainers = listOf(
                IngredientContainer(
                    Container(1, "냉장 1"),
                    listOf(
                        KindIngredientContainer(
                            IngredientKind.VEGETABLE, listOf(
                                ContainerIngredient(
                                    1,
                                    Container(1, "냉장 1"),
                                    Ingredient(1, "상추", IngredientKind.VEGETABLE, ""),
                                    LocalDateTime.now(),
                                    LocalDateTime.now()
                                ),
                                ContainerIngredient(
                                    1,
                                    Container(1, "냉장 1"),
                                    Ingredient(1, "상추", IngredientKind.VEGETABLE, ""),
                                    LocalDateTime.now(),
                                    LocalDateTime.now()
                                )
                            )
                        )
                    )
                ),
                IngredientContainer(
                    Container(1, "냉장 1"),
                    listOf(
                        KindIngredientContainer(
                            IngredientKind.VEGETABLE, listOf(
                                ContainerIngredient(
                                    1,
                                    Container(1, "냉장 1"),
                                    Ingredient(1, "상추", IngredientKind.VEGETABLE, ""),
                                    LocalDateTime.now(),
                                    LocalDateTime.now()
                                )
                            )
                        )
                    )
                )
            ),
            onContainerAddClicked = {},
            onKindContainerClicked = { _, _ -> },
            onAddIngredientClicked = {}
        )
    }
}

