package fr.isen.gauthier.projectgroup.Station

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import fr.isen.gauthier.projectgroup.R


class Bienvenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                        text = " Bienvenu ",
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
        }
    }
    companion object {
        val HOME_EXTRA_KEY = "HOME_EXTRA_KEY"
    }
}
