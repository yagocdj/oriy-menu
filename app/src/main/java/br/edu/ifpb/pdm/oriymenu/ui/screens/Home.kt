package br.edu.ifpb.pdm.oriymenu.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.MealNames
import br.edu.ifpb.pdm.oriymenu.model.data.WeekDayNames
import br.edu.ifpb.pdm.oriymenu.ui.components.DishCard
import br.edu.ifpb.pdm.oriymenu.ui.components.SelectComponent
import br.edu.ifpb.pdm.oriymenu.ui.viewmodels.MenuViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
