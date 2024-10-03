package br.edu.ifpb.pdm.oriymenu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.ui.viewmodels.MenuViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    menuViewModel: MenuViewModel = viewModel(),
    onNavigateToFeedback: (Dish) -> Unit // Parâmetro para navegação para a tela de feedback
) {
    val scope = rememberCoroutineScope()
    //    val dishDAO = DishDAO()
    //    val menuDAO = MenuDAO()
    //    val dishes = remember { mutableStateListOf<Dish>() }
    val dishes by menuViewModel.dishes.collectAsState() // Coletando os pratos do ViewModel

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Passa a função onNavigateToFeedback para o DishCard
        DishCard(dishes = dishes, onFeedbackClick = onNavigateToFeedback) // Adicionado o parâmetro de clique
        Spacer(modifier = Modifier.height(16.dp))
        // FIXME: this button will be removed in the future as it is only for testing purposes
        // the data will be fetched from the database automatically
        //        Button(onClick = {
        //           scope.launch(Dispatchers.IO) { menuViewModel.fetchDishes() }
        //        }) { Text("Mostrar cardápio") }
        LaunchedEffect(scope) {
            menuViewModel.fetchDishes() // Carregar pratos ao iniciar
        }
    }
}

@Composable
fun DishCard(
    dishes: List<Dish>,
    onFeedbackClick: (Dish) -> Unit // Parâmetro para o clique de feedback
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
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dish.description,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Botão de Feedback
                    Button(onClick = { onFeedbackClick(dish) }) { // Chama a função de navegação ao clicar
                        Text("Dar Feedback")
                    }
                }
            }
        }
    }
}
