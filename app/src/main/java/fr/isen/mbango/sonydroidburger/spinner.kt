package fr.isen.mbango.sonydroidburger

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun Spinner(
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = stringArrayResource(id = R.array.burger_list)
    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemSelected(index) },
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp, // spécifiez la taille de la police
                    textAlign = TextAlign.Left // spécifiez l'alignement du text

                )
            )
        }
    }
}


