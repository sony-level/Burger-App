package fr.isen.mbango.sonydroidburger

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun CommandeBurger(navController: NavController) {
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var nomState by remember { mutableStateOf("") }
    var prenomState by remember { mutableStateOf("") }
    var adresseState by remember { mutableStateOf("") }
    var phoneNumberState = remember { mutableStateOf("") }
    var selectedBurgerState by remember { mutableStateOf("") }
    var heureLivraisonState by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    val context = LocalContext.current
    var commandeEnvoyeeAvecSucces = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

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
        NomTextField(value = nomState, onValueChange = { nomState = it })
        PrenomTextField(value = prenomState, onValueChange = { prenomState = it })
        AdresseTextField(value = adresseState, onValueChange = { adresseState = it })
        PhoneNumberTextField(
            phoneNumberState = phoneNumberState,
            label = "Numéro de téléphone",
            modifier = Modifier.fillMaxWidth()
        )
        BurgerDropdown(
            selectedBurgerState = selectedBurgerState,
            onSelectedBurgerChange = { selectedBurgerState = it })
        Button(
            onClick = { showTimePickerDialog = true },
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
        ) {
            Text("Heure de livraison")
        }
        if (showTimePickerDialog) {
            ShowTimePicker(initialTime = selectedTime, onTimeSelected = { time ->
                selectedTime = time
                heureLivraisonState = time.toString() // Mise à jour du champ de saisie
                showTimePickerDialog = false
            })
        }

        Button(onClick = {
            if (validerChamps(
                    nomState,
                    prenomState,
                    adresseState,
                    phoneNumberState,
                    selectedBurgerState,
                    heureLivraisonState
                )
            ) {
                val commandeJson = creerJsonCommande(
                    nomState,
                    prenomState,
                    adresseState,
                    phoneNumberState,
                    selectedBurgerState,
                    heureLivraisonState
                )


                coroutineScope.launch {
                    val message = EnvoyerCommandeAuServeur(commandeJson, 356, context)
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    if (message == "Commande envoyée avec succès") {
                        commandeEnvoyeeAvecSucces.value = true
                        navController.navigate("confirmationRoute") // navigate to confirmation page
                    }
                }
            } else {
                Toast.makeText(context, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT)
                    .show()
            }
        }, modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()) {
            Text("Valider la commande")
        }

        // Affiche un message si la commande a été envoyée avec succès
        if (commandeEnvoyeeAvecSucces.value) {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Commande envoyée avec succès", Toast.LENGTH_LONG).show()
                commandeEnvoyeeAvecSucces.value =
                    false
            }
        }

    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "mainRoute") {
        composable("mainRoute") { CommandeBurger(navController)}
        composable("confirmationRoute") { ConfirmationPage() }
    }
}
@Composable
fun NomTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text("Nom") }, modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth())
}

@Composable
fun PrenomTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text("Prénom") }, modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth())
}

@Composable
fun AdresseTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text("Adresse") }, modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth())
}


@Composable
fun BurgerDropdown(
    selectedBurgerState: String,
    onSelectedBurgerChange: (String) -> Unit
) {
    val burgers = stringArrayResource(R.array.burger_list)

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Burger sélectionné : $selectedBurgerState",
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
                    onSelectedBurgerChange(burger)
                    expanded = false
                }) {
                    Text(burger)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowTimePicker(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    Button(onClick = {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                onTimeSelected(LocalTime.of(hourOfDay, minute))
            },
            initialTime.hour,
            initialTime.minute,
            true
        ).show()
    }) {
        Text("Ouvrir le sélecteur d'heure")
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
                     phoneNumberState.value = phoneNumberState.value.dropLast(1)
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
        )

        // Example phone number text in the background
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Ex: +33 6 12 34 56 78",
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
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
fun validerChamps(nom: String, prenom: String, adresse: String, phoneNumber: MutableState<String>, selectedBurger: String, heureLivraison: String): Boolean {
    return nom.isNotBlank() && prenom.isNotBlank() && adresse.isNotBlank() && phoneNumber.value.isNotBlank()
            && selectedBurger.isNotBlank() && heureLivraison.isNotBlank()
}



fun creerJsonCommande(
    nom: String,
    prenom: String,
    adresse: String,
    numeroTelephone: MutableState<String>,
    burgerSelectionne: String,
    heureLivraison: String): String {
    val commande = mapOf(
        "nom" to nom, "prenom" to prenom,
        "adresse" to adresse,
        "numeroTelephone" to numeroTelephone,
        "burgerSelectionne" to burgerSelectionne,
        "heureLivraison" to heureLivraison)
    return JSONObject(commande).toString()
}


suspend fun EnvoyerCommandeAuServeur(
    commandeJson: String,
    idUser: Int,
    context: Context
): String {
    val url = "http://test.api.catering.bluecodegames.com/user/order"
    val jsonObject = JSONObject().apply {
        put("id_shop", "1")
        put("id_user", idUser)
        put("msg", commandeJson)
    }

    val client = OkHttpClient()
    val requestBody = jsonObject.toString().toRequestBody()
    val request = Request.Builder().url(url).post(requestBody).build()

    return try {
        val response = client.newCall(request).execute()
        val message = response.body?.string() ?: "No response"
        if (response.isSuccessful) {
            "Commande envoyée avec succès"
        } else {
            "La commande a échoué: $message"
        }
    } catch (e: IOException) {
        "Request failed: ${e.message}"
    }
}


/** @Composable
 fun EnvoyeCommandeAuServeur(
    commandeJson: String,
    idUser: Int,
    context: Context,
    commandeEnvoyeeAvecSucces: MutableState<Boolean>
) {
    var toastMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = toastMessage) {
        val url = "http://test.api.catering.bluecodegames.com/user/order"
        val jsonObject = JSONObject().apply {
            put("id_shop", "1")
            put("id_user", idUser)
            put("msg", commandeJson)
        }

        val client = OkHttpClient()
        val requestBody = jsonObject.toString().toRequestBody()
        val request = Request.Builder().url(url).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                // Traitement de la réponse
                val message = response.body?.string() ?: "No response"
                toastMessage = message
                if (response.isSuccessful) {
                    commandeEnvoyeeAvecSucces.value = true
                } else {
                    commandeEnvoyeeAvecSucces.value = false
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                toastMessage = "Request failed: ${e.message}"
                commandeEnvoyeeAvecSucces.value = false
            }
        })
    }
    if (toastMessage.isNotBlank()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        toastMessage = ""
    }
} */