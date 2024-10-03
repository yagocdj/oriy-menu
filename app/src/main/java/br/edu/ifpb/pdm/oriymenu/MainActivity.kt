package br.edu.ifpb.pdm.oriymenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.ifpb.pdm.oriymenu.ui.theme.OriymenuTheme
import br.edu.ifpb.pdm.oriymenu.ui.screens.HomeScreen
import br.edu.ifpb.pdm.oriymenu.ui.viewmodels.MenuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OriymenuTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(menuViewModel: MenuViewModel = viewModel()) {
    // Sincroniza os dados do Firestore com Room
    menuViewModel.syncData()

    // Observa o estado dos menus e pratos
    val menus = menuViewModel.menus.collectAsState()
    val dishes = menuViewModel.dishes.collectAsState()

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
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val navController = rememberNavController()
        val startScreen = "home"

        NavHost(navController = navController, startDestination = startScreen) {
            composable("home") {
                // Passa os menus e pratos para a HomeScreen
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    menus = menus.value,
                    dishes = dishes.value
                )
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
