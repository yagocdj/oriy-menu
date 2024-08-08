package br.edu.ifpb.pdm.oriymenu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import br.edu.ifpb.pdm.oriymenu.ui.theme.OriymenuTheme
import br.edu.ifpb.pdm.oriymenu.ui.theme.TelaLogin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OriymenuTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaLogin()
                }
            }
        }
    }
}
