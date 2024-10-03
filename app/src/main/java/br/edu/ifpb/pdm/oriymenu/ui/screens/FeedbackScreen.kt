package br.edu.ifpb.pdm.oriymenu.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import br.edu.ifpb.pdm.oriymenu.model.data.Dish

@Composable
fun FeedbackScreen(dish: Dish, onFeedbackSubmitted: (String) -> Unit, onCancel: () -> Unit) {
    var feedbackText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    // Obter a configuração da tela para calcular 20% da altura
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = screenHeight * 0.2f)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Feedback para: ${dish.name}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para o feedback
        TextField(
            value = feedbackText,
            onValueChange = {
                feedbackText = it
                isError = false
            },
            label = { Text("Digite seu feedback") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            isError = isError
        )

        // Exibe uma mensagem de erro se o campo estiver vazio
        if (isError) {
            Text(
                text = "O feedback não pode estar vazio.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botões de cancelar e enviar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
            Button(onClick = {
                if (feedbackText.isBlank()) {
                    isError = true
                    onFeedbackSubmitted(feedbackText)
                }
            }) {
                Text("Enviar")
            }
        }
    }
}




