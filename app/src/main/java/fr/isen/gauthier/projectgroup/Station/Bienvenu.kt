package fr.isen.gauthier.projectgroup.Station

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import fr.isen.gauthier.projectgroup.R


class Bienvenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketView()
        }
    }
    companion object {
        val HOME_EXTRA_KEY = "HOME_EXTRA_KEY"
    }
}

@Composable fun BasketView() {
    val context = LocalContext.current

    Surface (
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = " Bienvenue ",
                fontFamily = FontFamily.Default,
                fontSize = 44.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Image(
                painterResource(R.drawable.plan_des_pistes_alpin_molines_saint_veran_domaine_beauregard),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(200.dp) // Augmentation de la taille de l'image
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(elevation = 8.dp)
                .background(color = colorResource(id = R.color.purple_500))
        ) {
            OutlinedButton(onClick = {
                Toast.makeText(context, "on dessand", Toast.LENGTH_LONG).show()
            }){
                Text(
                    text = "Piste"
                )
            }
            OutlinedButton(onClick = {
                Toast.makeText(context, "on monte", Toast.LENGTH_LONG).show()
            }){
                Text(
                    text = "Remonter"
                )
            }
            OutlinedButton(onClick = {
                Toast.makeText(context, "ici", Toast.LENGTH_LONG).show()
            }){
                Text(
                    text = "Menu"
                )
            }
            OutlinedButton(onClick = {
                Toast.makeText(context, "blabla", Toast.LENGTH_LONG).show()
            }){
                Text(
                    text = "chat"
                )
            }
            OutlinedButton(onClick = {
                Toast.makeText(context, "refraiche", Toast.LENGTH_LONG).show()
            }){
                Text(
                    text = "Refraiche"
                )
            }
            Spacer(Modifier.weight(1f))

        }
    }
}

