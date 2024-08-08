package br.edu.ifpb.pdm.oriymenu.ui.theme.telas

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.ifpb.pdm.oriymenu.ui.theme.OriymenuTheme

@Composable
fun RegisterScreen(modifier: Modifier = Modifier, onRegisterSuccessClick: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var enrollment by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Cadastro",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(text = "Nome completo") },
            placeholder = { Text(text = "Seu nome completo") }
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "E-mail") },
            placeholder = { Text(text = "example@mail.com") }
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = enrollment,
            onValueChange = { enrollment = it },
            label = { Text(text = "Matrícula") },
            placeholder = { Text(text = "000000-0") }
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Senha") }
        )
        Button(onClick = {
            // After successful registration logic
            onRegisterSuccessClick()
        }) {
            Text("Registrar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    OriymenuTheme {
        RegisterScreen(onRegisterSuccessClick = {}) }
}