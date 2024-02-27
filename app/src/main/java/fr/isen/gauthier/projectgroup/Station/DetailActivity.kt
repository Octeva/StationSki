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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Station.PisteActivity
import fr.isen.gauthier.projectgroup.Network.PisteCategory
import fr.isen.gauthier.projectgroup.R
import fr.isen.gauthier.projectgroup.ui.theme.ProjectGroupTheme
import java.io.Serializable
import kotlin.math.max
import fr.isen.gauthier.projectgroup.Network.Piste as Piste1


class DetailActivity : ComponentActivity() {
    companion object {
        val DETAIL_EXTRA_KEY = "DETAIL_EXTRA_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val piste = intent.getSerializableExtra(DETAIL_EXTRA_KEY) as? Piste

        super.onCreate(savedInstanceState)

        setContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                piste?. let {
                    Text(
                        text = it.name,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 15.dp)
                    )
                }

                Text(
                    text = "Renseignement sur la piste"
                )

                //Couleur de la piste
                //Etat de la piste en fonction du dernier utilisateur qui a renseigné
                //Affluence sur la piste en fonction du dernier utilisateur qui a renseigné
                // Meteo sur la piste en fonction du dernier utilisateur qui a renseigné



            StatePiste(Piste())

            }
        }
    }
}



@Composable
fun StatePiste(piste: Piste) {
    val etatText = remember { mutableStateOf("") }
    val affluenceText = remember { mutableStateOf("") }
    val weatherText = remember { mutableStateOf("") }
    var context = LocalContext.current

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

    Column(modifier = Modifier.padding(top = 30.dp)) {
        Text(text = "Quel est la meteo sur cette piste ? ${weatherText.value}",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(10.dp))

        Row {
            Button(onClick = {
                piste.visibility = 0
                weatherText.value = "Soleil"
            }) {
                Text(text = "Soleil")
            }

            Button(onClick = {
                piste.visibility = 1
                weatherText.value = "Nuageux"
            }) {
                Text(text = "Nuage")
            }

            Button(onClick = {
                piste.visibility = 2
                weatherText.value = "Brouillard"
            }) {
                Text(text = "Brouillard")
            }
        }
        Row {
            Button(onClick = {
                piste.visibility = 3
                weatherText.value = "Venteux"
            }) {
                Text(text = "Vent")
            }
            Button(onClick = {
                piste.visibility = 4
                weatherText.value = "Neige"
            }) {
                Text(text = "Neige")
            }
        }

    }

    Button(onClick = {
        Toast.makeText(context, "Piste ${piste.name} enregistrée", Toast.LENGTH_LONG).show()
    }) {
        Text(text = "Enregistrer")
    }
}

//Text(text = "Quel est l'affluence sur ${piste.name} : ${affluenceText.value}",
