package fr.isen.mbango.sonydroidburger

import android.annotation.SuppressLint
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber


@SuppressLint("UnrememberedMutableState")
@Composable
fun CommandeBurger() {

    val phoneNumberState = remember { mutableStateOf("") }


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



        PhoneNumberTextField(
            phoneNumberState = phoneNumberState,
            label = "Numéro de téléphone",
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

@Composable
fun PhoneNumberTextField(
    phoneNumberState: MutableState<String>,
    label: String,
    modifier: Modifier = Modifier
) {
    val phoneNumberUtil = remember { PhoneNumberUtil.getInstance() }
    var selectedCountryCode by remember { mutableStateOf("FR") }

    val countries = remember { // Liste des codes indicatifs téléphoniques des pays de l'Union européenne
        listOf(
            "AT" to "Austria (+43)",
            "BE" to "Belgium (+32)",
            "BG" to "Bulgaria (+359)",
            "CY" to "Cyprus (+357)",
            "CZ" to "Czech Republic (+420)",
            "DE" to "Germany (+49)",
            "DK" to "Denmark (+45)",
            "EE" to "Estonia (+372)",
            "EL" to "Greece (+30)",
            "ES" to "Spain (+34)",
            "FI" to "Finland (+358)",
            "FR" to "France (+33)",
            "HR" to "Croatia (+385)",
            "HU" to "Hungary (+36)",
            "IE" to "Ireland (+353)",
            "IT" to "Italy (+39)",
            "LT" to "Lithuania (+370)",
            "LU" to "Luxembourg (+352)",
            "LV" to "Latvia (+371)",
            "MT" to "Malta (+356)",
            "NL" to "Netherlands (+31)",
            "PL" to "Poland (+48)",
            "PT" to "Portugal (+351)",
            "RO" to "Romania (+40)",
            "SE" to "Sweden (+46)",
            "SI" to "Slovenia (+386)",
            "SK" to "Slovakia (+421)"
        )
    }

    var dropdownDismissed by remember { mutableStateOf(false) }
    Column {
        // Liste déroulante pour sélectionner le code indicatif téléphonique
        DropdownMenu(
            expanded = true, // Changer à true pour le rendre visible par défaut
            onDismissRequest = {
                dropdownDismissed = true
            }
        ) {
            countries.forEach { (countryCode, countryName) ->
                DropdownMenuItem(onClick = { selectedCountryCode = countryCode }) {
                    Text(countryName)
                }
            }
        }
        OutlinedTextField(
            value = phoneNumberState.value,
            onValueChange = { phoneNumber ->
                phoneNumberState.value = phoneNumber
                if (phoneNumber.isBlank()) return@OutlinedTextField
                val parsedNumber = try {
                    phoneNumberUtil.parseAndKeepRawInput(phoneNumber, selectedCountryCode)
                } catch (e: Exception) {
                    null
                }
                if (parsedNumber != null && !phoneNumberUtil.isValidNumber(parsedNumber)) {
                    phoneNumberState.value = phoneNumberState.value.dropLast(8)
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
        )
    }
}







