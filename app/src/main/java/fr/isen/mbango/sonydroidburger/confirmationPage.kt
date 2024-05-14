package fr.isen.mbango.sonydroidburger


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@Composable
fun ConfirmationPage() {
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
            text = "Confirmation de commande",
            style = MaterialTheme.typography.overline,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Votre commande a été passée avec succès!",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        ListeCommandes()
    }
}



fun getListeCommandes(idUser: Int): List<String> {
    val url = "http://test.api.catering.bluecodegames.com/listorders"
    val jsonObject = JSONObject().apply {
        put("id_shop", "1")
        put("id_user", idUser)
    }

    val client = OkHttpClient()
    val requestBody = jsonObject.toString().toRequestBody()
    val request = Request.Builder().url(url).post(requestBody).build()

    val response = client.newCall(request).execute()
    return if (response.isSuccessful) {
        val responseData = response.body?.string() ?: ""
        parseListeCommandes(responseData)
    } else {
        emptyList()
    }
}

@Serializable
data class Order(
    val orderId: Int,
    val burgerName: String,
    val deliveryTime: String
)

fun parseListeCommandes(responseData: String): List<String> {
    val orders = Json.decodeFromString<List<Order>>(responseData)
    val commandes = mutableListOf<String>()

    orders.forEach { order ->
        val commandeText = "Commande #${order.orderId}: ${order.burgerName}, Livraison à ${order.deliveryTime}"
        commandes.add(commandeText)
    }
    return commandes
}

@Composable
fun ListeCommandes() {
    val idUtilisateur = 356 // Utilisateur actuel, vous pouvez récupérer cette valeur à partir de l'interface utilisateur

    val commandes = remember { getListeCommandes(idUtilisateur) }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Vos commandes précédentes :",
            style = MaterialTheme.typography.subtitle1,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Afficher les commandes dans la liste
        commandes.forEach { commande ->
            Text(
                text = commande,
                style = MaterialTheme.typography.body2,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
