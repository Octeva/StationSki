package fr.isen.gauthier.projectgroup.Station

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Network.PisteCategory
import fr.isen.gauthier.projectgroup.R
import fr.isen.gauthier.projectgroup.ui.theme.ProjectGroupTheme
import kotlin.math.max
import fr.isen.gauthier.projectgroup.Network.Piste as Piste1


class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {


            StatePiste(Piste())
            Affluence(Piste())
            }
        }
    }

    override fun onPause(){
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}


@Composable
fun StatePiste(piste: Piste) {
    val etatText = remember { mutableStateOf(if (piste.etat) "Ouvert" else "Fermé") }

    Row(modifier = Modifier.padding(top = 30.dp)) {
        Text(text = "Piste ${piste.name} ${etatText.value}",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(10.dp))
        Button(onClick = {
            piste.etat = true
            etatText.value = "Ouvert"
        }) {
            Text(text = "Ouvert")
        }

        Button(onClick = {
            piste.etat = false
            etatText.value = "Fermé"
        }) {
            Text(text = "Fermé")
        }
    }
}

//Text(text = "Quel est l'affluence sur ${piste.name} : ${affluenceText.value}",
@Composable
fun Affluence(piste: Piste) {
    val affluenceText = remember { mutableStateOf("Peu") }

   Column(modifier = Modifier.padding(top = 30.dp)) {
        Text(text = "Quel est l'affluence sur cette piste ? ${affluenceText.value}",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(10.dp))
       Row {


        Button(onClick = {
            piste.affluence = 0
            affluenceText.value = "Peu"
        }) {
            Text(text = "Peu")
        }

        Button(onClick = {
            piste.affluence = 1
            affluenceText.value = "Moyen"
        }) {
            Text(text = "Moyen")
        }

        Button(onClick = {
            piste.affluence = 2
            affluenceText.value = "Beaucoup"
        }) {
            Text(text = "Beaucoup")
        }
    }
   }
}