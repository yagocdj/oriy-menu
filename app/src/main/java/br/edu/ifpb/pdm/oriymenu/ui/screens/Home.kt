package br.edu.ifpb.pdm.oriymenu.ui.screens


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.pdm.oriymenu.R
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.WeekDayNames
import br.edu.ifpb.pdm.oriymenu.ui.viewmodels.MenuViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    menuViewModel: MenuViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val dishes by menuViewModel.dishes.collectAsState()

    val namesOfDaysOfWeek = listOf(
        WeekDayNames.MONDAY.dayName, WeekDayNames.TUESDAY.dayName, WeekDayNames.WEDNESDAY.dayName,
        WeekDayNames.THURSDAY.dayName, WeekDayNames.FRIDAY.dayName
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SelectComponent(modifier, elements = namesOfDaysOfWeek, onSelect = { index ->
            scope.launch(Dispatchers.IO) {
                menuViewModel.fetchByDayOfWeek(namesOfDaysOfWeek[index])
            }
        })
        Spacer(modifier = Modifier.height(20.dp))

        DishCard(dishes = dishes)
        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(scope) {
            menuViewModel.fetchByDayOfWeek(namesOfDaysOfWeek[0])
        }
    }
}

@Composable
fun DishCard(
    dishes: List<Dish>
) {

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
            )
            {
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
                    Text(
                        text = dish.description,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SelectComponent(
    modifier: Modifier = Modifier,
    elements: List<String>,
    menuViewModel: MenuViewModel = viewModel(),
    onSelect: (Int) -> Unit,
) {
    val isDropDownExpanded by menuViewModel.isDropDownExpanded.collectAsState()
    val currentDayIndex by menuViewModel.selectedElementIndex.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    menuViewModel.showDropDown()
                }
                    .padding(12.dp)
            ) {
                Text(text = elements[currentDayIndex])
                Image(
                    painter = painterResource(id = R.drawable.arrow_drop_down),
                    contentDescription = "Arrow Drop Down"
                )
            }
        }
        DropdownMenu(
            modifier = Modifier.padding(horizontal = 16.dp),
            expanded = isDropDownExpanded,
            onDismissRequest = {
                menuViewModel.collapseDropDown()
            }
        ) {
            elements.forEachIndexed { index, name ->
                DropdownMenuItem(text = {
                    Text(text = name)
                },
                    onClick = {
                        menuViewModel.collapseDropDown()
                        menuViewModel.changeSelectedElementIndex(index)
                        onSelect(index)
                    })
            }
        }
    }
}
