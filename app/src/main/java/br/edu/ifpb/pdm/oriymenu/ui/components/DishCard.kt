package br.edu.ifpb.pdm.oriymenu.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import coil.compose.AsyncImage

@Composable
fun DishCard(
    dishes: List<Dish>,
    onFeedbackClick: (Dish) -> Unit
) {
    // Estado para controlar o prato atualmente selecionado para exibição no AlertDialog
    val (selectedDish, setSelectedDish) = remember { mutableStateOf<Dish?>(null) }

    if (dishes.isEmpty()) {
        // Exibe a mensagem de "Sem pratos disponíveis" quando a lista estiver vazia
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info, // Ícone de informação
                contentDescription = "Sem pratos disponíveis",
                tint = MaterialTheme.colorScheme.primary, // Cor do ícone
                modifier = Modifier.size(64.dp) // Tamanho do ícone
            )
            Spacer(modifier = Modifier.height(16.dp)) // Espaço entre ícone e texto
            Text(
                text = "Não há pratos disponíveis para este dia.",
                style = MaterialTheme.typography.bodyLarge, // Estilo do texto
                color = MaterialTheme.colorScheme.onSurface // Cor do texto
            )
        }
    } else {
        // Exibe a lista de pratos quando houver itens disponíveis
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
                        Text(text = dish.meal, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))

                        // Row para alinhar o botão "Dar Feedback" e o ícone "Ver Detalhes"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botão "Dar Feedback"
                            Button(onClick = { onFeedbackClick(dish) }) {
                                Text("Dar Feedback")
                            }

                            // Botão redondo com ícone de informação
                            IconButton(
                                onClick = { setSelectedDish(dish) }, // Abre o modal com os detalhes do prato
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info, // Ícone de "info"
                                    contentDescription = "Ver Detalhes",
                                    modifier = Modifier.size(24.dp) // Tamanho do ícone
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Exibe o AlertDialog quando um prato é selecionado
    selectedDish?.let { dish ->
        AlertDialog(
            onDismissRequest = { setSelectedDish(null) }, // Fecha o modal ao clicar fora
            title = { Text("Detalhes do Prato") }, // Título do modal
            text = { // Conteúdo do modal
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Exibe a imagem do prato
                    AsyncImage(
                        model = dish.pathToImage,
                        contentDescription = dish.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Exibe os detalhes do prato
                    Text("Nome: ${dish.name}", style = MaterialTheme.typography.titleMedium)
                    Text("Refeição: ${dish.meal}", style = MaterialTheme.typography.bodyMedium)
                    Text("Descrição: ${dish.description}", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = { // Botão para fechar o modal
                Button(onClick = { setSelectedDish(null) }) {
                    Text("Fechar")
                }
            }
        )
    }
}
