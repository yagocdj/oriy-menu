package br.edu.ifpb.pdm.oriymenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.edu.ifpb.pdm.oriymenu.ui.theme.OriymenuTheme
import br.edu.ifpb.pdm.oriymenu.ui.theme.telas.HomeScreen
import br.edu.ifpb.pdm.oriymenu.ui.theme.telas.LoginScreen
import br.edu.ifpb.pdm.oriymenu.ui.theme.telas.RegisterScreen


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OriymenuTheme {
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
                    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(onSignInClick = {
                                navController.navigate("home")
                            }, onRegisterClick = {
                                navController.navigate("register")
                            })
                        }
                        composable("home") {
                            HomeScreen(modifier = Modifier.padding(innerPadding), onLogoffClick = {
                                navController.navigate("login")
                            })
                        }
                        composable("register") {
                            RegisterScreen(modifier = Modifier.padding(innerPadding), onRegisterSuccessClick = {
                                navController.navigate("home")
                            })
                        }
                    }
                }
            }
        }
    }
}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    OriymenuTheme {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    colors = topAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer,
//                        titleContentColor = MaterialTheme.colorScheme.primary,
//                    ),
//                    title = {
//                        Text("Navegação")
//                    },
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//            }, modifier = Modifier.fillMaxSize()) { innerPadding ->
//            val navController = rememberNavController()
//            NavHost(navController = navController, startDestination = "login") {
//                composable("login") {
//                    LoginScreen(onSignInClick = {
//                        navController.navigate("home")
//                    }, onRegisterClick = {
//                        navController.navigate("register")
//                    })
//                }
//                composable("home") {
//                    HomeScreen(modifier = Modifier.padding(innerPadding), onLogoffClick = {
//                        navController.navigate("login")
//                    })
//                }
//                composable("register") {
//                    RegisterScreen(modifier = Modifier.padding(innerPadding), onRegisterSuccessClick = {
//                        navController.navigate("home")
//                    })
//                }
//            }
//        }
//    }
//}

