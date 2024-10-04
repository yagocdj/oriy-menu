package br.edu.ifpb.pdm.oriymenu.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.pdm.oriymenu.R
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.MealNames
import br.edu.ifpb.pdm.oriymenu.model.data.WeekDayNames
import br.edu.ifpb.pdm.oriymenu.ui.viewmodels.MenuViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    menuViewModel: MenuViewModel = viewModel(),
    onNavigateToFeedback: (Dish) -> Unit // Parâmetro para navegação para a tela de feedback
) {
    val scope = rememberCoroutineScope()
    val dishes by menuViewModel.dishes.collectAsState()

    val namesOfDaysOfWeek = listOf(
        WeekDayNames.MONDAY.dayName, WeekDayNames.TUESDAY.dayName, WeekDayNames.WEDNESDAY.dayName,
        WeekDayNames.THURSDAY.dayName, WeekDayNames.FRIDAY.dayName
    )
    // 0 -> breakfast, 1 -> lunch
    val mealTypes = listOf(MealNames.BREAKFAST.mealName, MealNames.LUNCH.mealName)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            // Select the day of the week
            Column {
                Text(text = "Dia da semana")
                SelectComponent(
                    elements = namesOfDaysOfWeek,
                    isDropDownExpanded = menuViewModel.isDayDropdownExpanded,
                    onShowDropDown = { menuViewModel.showDayDropdown() },
                    onCollapseDropDown = { menuViewModel.collapseDayDropdown() },
                    currentElementIndex = menuViewModel.selectedDayIndex,
                    onSelect = { index ->
                        menuViewModel.changeSelectedDayIndex(index)
                        scope.launch(Dispatchers.IO) {
                            menuViewModel.fetchByDayOfWeek(namesOfDaysOfWeek[index])
                        }
                    })
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Select the meal
            Column {
                Text(text = "Refeição")
                SelectComponent(
                    elements = mealTypes,
                    isDropDownExpanded = menuViewModel.isMealDropDownExpanded,
                    onShowDropDown = { menuViewModel.showMealDropdown() },
                    onCollapseDropDown = { menuViewModel.collapseMealDropdown() },
                    currentElementIndex = menuViewModel.selectedMealIndex,
                    onSelect = { index ->
                        scope.launch(Dispatchers.IO) {
                            // Fetch dishes by selected day and meal type
                            menuViewModel.changeSelectedMealIndex(index)
                            val selectedDay = namesOfDaysOfWeek[
                                menuViewModel.selectedDayIndex.value]
                            menuViewModel.fetchByDayOfWeekAndMeal(selectedDay, mealTypes[index])
                        }
                    })
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Passa a função onNavigateToFeedback para o DishCard
        DishCard(dishes = dishes, onFeedbackClick = onNavigateToFeedback)
        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(scope) {
//            menuViewModel.fetchByDayOfWeek(namesOfDaysOfWeek[0])
            menuViewModel.fetchByDayOfWeekAndMeal(namesOfDaysOfWeek[0], mealTypes[0])
        }
    }
}

@Composable
fun DishCard(
    dishes: List<Dish>,
    onFeedbackClick: (Dish) -> Unit
) {
    // Estado para controlar o prato atualmente selecionado para exibição no AlertDialog
    val (selectedDish, setSelectedDish) = remember { mutableStateOf<Dish?>(null) }

    LazyColumn {
        items(dishes) { dish ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    AsyncImage(
                        model = dish.pathToImage,
                        contentDescription = dish.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = dish.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = dish.meal, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))

                    // Row para alinhar o botão "Dar Feedback" e o ícone "Ver Detalhes"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botão "Dar Feedback"
                        Button(onClick = { onFeedbackClick(dish) }) {
                            Text("Dar Feedback")
                        }

                        // Botão redondo com ícone de informação
                        IconButton(
                            onClick = { setSelectedDish(dish) }, // Abre o modal com os detalhes do prato
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info, // Ícone de "info"
                                contentDescription = "Ver Detalhes",
                                modifier = Modifier.size(24.dp) // Tamanho do ícone
                            )
                        }
                    }
                }
            }
        }
    }

    // Exibe o AlertDialog quando um prato é selecionado
    selectedDish?.let { dish ->
        AlertDialog(
            onDismissRequest = { setSelectedDish(null) }, // Fecha o modal ao clicar fora
            title = { Text("Detalhes do Prato") }, // Título do modal
            text = { // Conteúdo do modal
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Exibe a imagem do prato
                    AsyncImage(
                        model = dish.pathToImage,
                        contentDescription = dish.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Exibe os detalhes do prato
                    Text("Nome: ${dish.name}", style = MaterialTheme.typography.titleMedium)
                    Text("Refeição: ${dish.meal}", style = MaterialTheme.typography.bodyMedium)
                    Text("Descrição: ${dish.description}", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = { // Botão para fechar o modal
                Button(onClick = { setSelectedDish(null) }) {
                    Text("Fechar")
                }
            }
        )
    }
}



@Composable
fun SelectComponent(
    elements: List<String>,
    isDropDownExpanded: StateFlow<Boolean>,
    onShowDropDown: () -> Unit,
    onCollapseDropDown: () -> Unit,
    currentElementIndex: StateFlow<Int>,
    onSelect: (Int) -> Unit,
) {
    val isExpanded by isDropDownExpanded.collectAsState()
    val currentIndex by currentElementIndex.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        onShowDropDown()
                    }
                    .padding(12.dp)
            ) {
                Text(text = elements[currentIndex])
                Image(
                    painter = painterResource(id = R.drawable.arrow_drop_down),
                    contentDescription = "Arrow Drop Down"
                )
            }
        }
        DropdownMenu(
            modifier = Modifier.padding(horizontal = 16.dp),
            expanded = isExpanded,
            onDismissRequest = {
                onCollapseDropDown()
            }
        ) {
            elements.forEachIndexed { index, name ->
                DropdownMenuItem(text = {
                    Text(text = name)
                },
                    onClick = {
                        onCollapseDropDown()
                        onSelect(index)
                    })
            }
        }
    }
}

