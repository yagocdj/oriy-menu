package br.edu.ifpb.pdm.oriymenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.ui.screens.FeedbackScreen
import br.edu.ifpb.pdm.oriymenu.ui.screens.HomeScreen
import br.edu.ifpb.pdm.oriymenu.ui.theme.OriymenuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OriymenuTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("ORYI Menu")
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val navController = rememberNavController()
        val dishDAO = remember { DishDAO() } // Instancia do DishDAO

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onNavigateToFeedback = { dish ->
                        navController.navigate("feedback/${dish.id}")
                    }
                )
            }
            composable("feedback/{dishId}") { backStackEntry ->
                val dishId = backStackEntry.arguments?.getString("dishId")

                if (dishId != null) {
                    var dish by remember { mutableStateOf<Dish?>(null) } // Use mutableStateOf para armazenar o prato

                    LaunchedEffect(dishId) {
                        dishDAO.findById(dishId) { fetchedDish ->
                            dish = fetchedDish
                        }
                    }

                    if (dish == null) {
                        // Exibir um indicador de carregamento
                        CircularProgressIndicator()
                    } else {
                        // Exibe a tela de feedback quando o prato é encontrado
                        FeedbackScreen(
                            dish = dish!!, // Usar o operador !! pois dish não pode ser nulo aqui
                            onFeedbackSubmitted = { },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                } else {
                    // Lidar com o caso onde dishId é nulo (opcional)
                    navController.popBackStack() // Voltar à tela anterior
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    OriymenuTheme {
        MainApp()
    }
}
