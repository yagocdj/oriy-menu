package br.edu.ifpb.pdm.oriymenu.ui.screens

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.model.data.Feedback
import br.edu.ifpb.pdm.oriymenu.model.data.FeedbackDAO
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException


@Composable
fun FeedbackScreen(
    dish: Dish,
    onFeedbackSubmitted: (String, Uri?) -> Unit,
    onCancel: () -> Unit
) {
    var feedbackText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var feedbacks by remember { mutableStateOf<List<Feedback>>(emptyList()) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(dish) {
        FeedbackDAO().getFeedbacksForDish(dish) { fetchedFeedbacks ->
            feedbacks = fetchedFeedbacks
            Log.d("!! FEEDBACKS !!", fetchedFeedbacks.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = screenHeight * 0.15f) // Reduzi a altura para deixar mais visível
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Feedback para: ${dish.name}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Campo de texto para o feedback
        OutlinedTextField(
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

        if (isError) {
            Text(
                text = "O feedback não pode estar vazio.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para abrir a câmera com ícone
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    Log.d("Upload URI", imageUri.toString())
                    imageUri?.let {
                        FeedbackDAO().uploadImage(it, dish.name) { imageUrl ->
                            if (imageUrl != null) {
                                val newFeedback = Feedback(
                                    dish = dish.id,
                                    description = feedbackText,
                                    pathToImage = imageUrl
                                )
                                FeedbackDAO().save(newFeedback) { docRef ->
                                    if (docRef != null) {
                                        dish.feedback += docRef.id
                                        DishDAO().update(dish) { success ->
                                            onFeedbackSubmitted(feedbackText, imageUri)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )

        Button(
            onClick = {
                imageUri = createImageUri(context)
                launcher.launch(imageUri!!)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add, // Ícone de câmera
                contentDescription = "Ícone de câmera",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Tirar Foto")
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
                } else {
                    onFeedbackSubmitted(feedbackText, imageUri)
                }
            }) {
                Text("Enviar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar feedbacks
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(feedbacks) { feedback ->
                FeedbackCard(feedback = feedback)
            }
        }
    }
}

@Composable
fun FeedbackCard(feedback: Feedback) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = feedback.description ?: "Sem descrição",
                style = MaterialTheme.typography.bodyLarge
            )

            feedback.pathToImage?.let { imageUrl ->
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


fun createImageUri(context: Context): Uri {
    // Cria um arquivo temporário para armazenar a imagem
    val photoFile: File = try {
        // Cria um arquivo no diretório de imagens da aplicação
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        File.createTempFile(
            "photo_${System.currentTimeMillis()}", // Prefixo do nome do arquivo
            ".jpg", // Sufixo do nome do arquivo
            storageDir // Diretório onde o arquivo será criado
        )
    } catch (e: IOException) {
        e.printStackTrace()
        // Retorna null se houver uma exceção ao criar o arquivo
        return Uri.EMPTY
    }

    // Retorna o URI que será usado pelo launcher
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider", // Nome do provedor definido no AndroidManifest.xml
        photoFile // O arquivo criado
    )
}

fun uploadImage(imageUri: Uri, dishName: String, callback: (String?) -> Unit) {
    val storage = FirebaseStorage.getInstance().reference
    val imageRef = storage.child("images/${dishName}.jpg")

    imageRef.putFile(imageUri)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString()) // URL da imagem no Firebase Storage
            }
        }
        .addOnFailureListener {
            callback(null) // Falha no upload
        }
}