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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.pdm.oriymenu.R
import br.edu.ifpb.pdm.oriymenu.database.entities.DishEntity
import br.edu.ifpb.pdm.oriymenu.database.entities.MenuEntity
import br.edu.ifpb.pdm.oriymenu.model.data.MealNames
import br.edu.ifpb.pdm.oriymenu.model.data.WeekDayNames
import br.edu.ifpb.pdm.oriymenu.ui.viewmodels.MenuViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    menus: List<MenuEntity>,
    dishes: List<DishEntity>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Menus")
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(menus) { menu ->
                MenuCard(menu = menu)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Dishes")
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(dishes) { dish ->
                DishCard(dish = dish)
            }
        }
    }
}

@Composable
fun MenuCard(menu: MenuEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Menu ID: ${menu.id}")
            Text(text = "Date: ${menu.date}")
        }
    }
}

@Composable
fun DishCard(dish: DishEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Dish Name: ${dish.name}")
            Text(text = "Meal: ${dish.meal}")
            Text(text = "Description: ${dish.description}")
        }
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
