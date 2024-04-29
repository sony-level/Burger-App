package fr.isen.mbango.sonydroidburger

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp



@Composable
fun CommandeBurger() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.droidburger_logo),
            contentDescription = "Logo DroidBurger",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Commande de Burger",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = "",
            onValueChange = { /* Do something */ },
            label = { Text("Nom") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = "",
            onValueChange = { /* Do something */ },
            label = { Text("Prénom") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = "",
            onValueChange = { /* Do something */ },
            label = { Text("Adresse") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = "",
            onValueChange = { /* Do something */ },
            label = { Text("Numéro de téléphone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )



            var expanded by remember { mutableStateOf(false) }
            var selectedBurger by remember { mutableStateOf(" ") }
            val burgers = listOf("Burger 1", "Burger 2", "Burger 3")

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Burger sélectionné : $selectedBurger",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .padding(16.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    burgers.forEach { burger ->
                        DropdownMenuItem(onClick = {
                            selectedBurger = burger
                            expanded = false
                        }) {
                            Text(text = burger)
                        }
                    }
                }
            }



        OutlinedTextField(
            value = "",
            onValueChange = { /* Do something */ },
            label = { Text("Heure de livraison (HH:MM)") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text("Valider la commande")
        }
    }
}

fun DropdownMenuItem(onClick: () -> Unit, interactionSource: @Composable () -> Unit) {

}






