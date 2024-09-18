package br.edu.ifpb.pdm.oriymenu.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.model.data.MenuDAO
import br.edu.ifpb.pdm.oriymenu.ui.theme.screens.ui.theme.OriymenuTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FirebaseTestComposable(modifier: Modifier = Modifier) {
    val menuId = "ixgMe0B7gG9lHZGDKRrM"
    val scope = rememberCoroutineScope()
    val menuDAO = MenuDAO()
    val dishDAO = DishDAO()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val dishes = remember { mutableStateListOf<Dish>() }

        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                menuDAO.findByIdWithDishesRefs(menuId, callback = { menu ->
                    if (menu != null) {
                        for (dishRef in menu.dishes) {
//                            dishRefs.add(dishRef)
                            dishDAO.findById(dishRef, callback = { dish ->
                                if (dish != null) {
                                    dishes.add(dish)
                                }
                            })
                        }
                    }
                })
            }
        }) {
            Text(text = "Obter dishes")
        }

        LazyColumn {
            items(dishes) { dish ->
                Text(text = "Dish name -> ${dish.name}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OriymenuTheme {
//        Greeting("Android")
    }
}
