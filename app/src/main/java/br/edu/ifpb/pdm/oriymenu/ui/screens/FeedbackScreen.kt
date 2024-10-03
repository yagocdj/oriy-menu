package br.edu.ifpb.pdm.oriymenu.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import br.edu.ifpb.pdm.oriymenu.model.data.Dish

@Composable
fun FeedbackScreen(
    dish: Dish,
    onFeedbackSubmitted: (String, Bitmap?) -> Unit, // Aqui deve estar definido corretamente
    onCancel: () -> Unit
) {
    var feedbackText by remember { mutableStateOf("") }
    var capturedImage: Bitmap? by remember { mutableStateOf(null) }

    // Lançador para abrir a câmera e capturar a imagem
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        capturedImage = bitmap // Atribui a imagem capturada
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Feedback para: ${dish.name}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Adicionando um Spacer para descer o TextField
        Spacer(modifier = Modifier.height(0.15.dp * 400)) // 15% de uma altura de tela padrão de 400dp

        TextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text("Digite seu feedback") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para abrir a câmera
        Button(onClick = { launcher.launch(null) }) {
            Text(text = "Tirar foto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibir a imagem capturada, se houver
        capturedImage?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Imagem capturada",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
            Button(onClick = {
                // Envia o feedback junto com a imagem
                onFeedbackSubmitted(feedbackText, capturedImage) // Esta linha deve funcionar agora
            }) {
                Text("Enviar")
            }
        }
    }
}

