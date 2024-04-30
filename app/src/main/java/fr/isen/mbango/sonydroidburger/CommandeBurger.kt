package fr.isen.mbango.sonydroidburger

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil


@SuppressLint("UnrememberedMutableState")
@Composable
fun CommandeBurger() {


    val nomState = remember { mutableStateOf("") }
    val prenomState = remember { mutableStateOf("") }
    val adresseState = remember { mutableStateOf("") }
    val phoneNumberState = remember { mutableStateOf("") }
    val selectedBurgerState = remember { mutableStateOf("") }
    val heureLivraisonState = remember { mutableStateOf("") }

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

        // Champ de saisie pour le nom
        OutlinedTextField(
            value = nomState.value,
            onValueChange = { nomState.value = it },
            label = { Text("Nom") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        // Champ de saisie pour le prénom
        OutlinedTextField(
            value = prenomState.value,
            onValueChange = { prenomState.value = it },
            label = { Text("Prénom") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        // Champ de saisie pour l'adresse
        OutlinedTextField(
            value = adresseState.value,
            onValueChange = { adresseState.value = it },
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

        BurgerDropdown(
            selectedBurgerState = selectedBurgerState,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        // Champ de saisie pour l'heure de livraison
        OutlinedTextField(
            value = heureLivraisonState.value,
            onValueChange = { heureLivraisonState.value = it },
            label = { Text("Heure de livraison (HH:MM)") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = {   if (validerCommande(
                    nomState.value,
                    prenomState.value,
                    adresseState.value,
                    phoneNumberState.value,
                    selectedBurgerState.value,
                    heureLivraisonState.value
                )
            ) {
                println("Commande validée")
            } else {
                // Sinon, affichez un message à l'utilisateur ou prenez une autre action appropriée
                println("Erreur dans la commande")
            } },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text("Valider la commande")
        }
    }
}


@Composable
fun PhoneNumberTextField(
    phoneNumberState: MutableState<String>,
    label: String,
    modifier: Modifier = Modifier
) {
    var selectedCountryCode by remember { mutableStateOf("FR") }
    var selectedCountryName by remember { mutableStateOf("France (+33)") }

    val phoneNumberUtil = remember { PhoneNumberUtil.getInstance() }
    val countries = remember { getCountryList() }

    Column {
        // Dropdown Menu for selecting country code
        DropdownMenu(
            expanded = false,
            onDismissRequest = { /* Dropdown dismissed */ }
        ) {
            countries.forEach { (countryCode, countryName) ->
                DropdownMenuItem(onClick = {
                    selectedCountryCode = countryCode
                    selectedCountryName = countryName
                }) {
                    Text(countryName)
                }
            }
        }

        // Text field for entering phone number
        OutlinedTextField(
            value = phoneNumberState.value,
            onValueChange = { phoneNumber ->
                phoneNumberState.value = phoneNumber
                if (phoneNumber.isBlank()) return@OutlinedTextField
                val parsedNumber = try {
                    phoneNumberUtil.parseAndKeepRawInput(phoneNumber, selectedCountryCode)
                } catch (e: NumberParseException) {
                    null
                }
                if (parsedNumber != null && !phoneNumberUtil.isValidNumber(parsedNumber)) {
                    // Handle invalid phone number
                    // Example: phoneNumberState.value = phoneNumberState.value.dropLast(1)
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
        )
    }
}

fun getCountryList(): List<Pair<String, String>> {
    return listOf(
        "FR" to "France (+33)",
        "US" to "United States (+1)",
        "GB" to "United Kingdom (+44)",
        "DE" to "Germany (+49)"
        // Add more countries as needed...
    )
}


@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            )
            .padding(contentPadding)
    ) {
        Row { content() }
    }
}

@Composable
fun BurgerDropdown(
    selectedBurgerState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val burgers = listOf(
        "Burger du chef",
        "Cheese Burger",
        "Burger Montagnard",
        "Burger Italien",
        "Burger Végétarien"
    )

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Burger sélectionné : ${selectedBurgerState.value}",
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
                    //selectedBurgerState.value = burger
                    expanded = false
                }) {
                    Text(burger)
                }
            }
        }
    }
}

fun validerCommande(
    nom: String,
    prenom: String,
    adresse: String,
    numeroTelephone: String,
    burgerSelectionne: String,
    heureLivraison: String
): Boolean {
    // Vérifier si tous les champs sont remplis
    if (nom.isBlank() || prenom.isBlank() || adresse.isBlank() || numeroTelephone.isBlank() || burgerSelectionne.isBlank() || heureLivraison.isBlank()) {
        return false
    }

    // Vérifier si le numéro de téléphone est un nombre
    if (!numeroTelephone.matches(Regex("\\d+"))) {
        return false
    }

    // Vérifier si le numéro de téléphone a une longueur valide
    if (numeroTelephone.length != 10) {
        return false
    }

    // Vérifier si l'heure de livraison est au bon format (HH:MM)
    val heureRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]\$")
    if (!heureLivraison.matches(heureRegex)) {
        return false
    }


    return true
}
