package fr.isen.gauthier.projectgroup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.gauthier.projectgroup.ui.theme.ProjectGroupTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectGroupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painterResource(R.drawable.skier___travers_des_paysages_magiques_aux_arcs),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth() // occupe toute la largeur disponible
                                .fillMaxSize()
                                .fillMaxHeight() // occupe toute la hauteur disponible
                                .padding(0.dp) // supprime le padding
                                //.scale(0.9f)
                        )// réduit la taille de l'image


                        //Greeting("Android")

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            AuthentificationScreen()
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun AuthentificationScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Connexion",
            //style = MaterialTheme.typography.h5
            style = TextStyle(
                fontFamily = FontFamily.Serif, // Changer la famille de police
                fontSize = 40.sp, // Changer la taille de la police en sp (scaled pixels)
                // Vous pouvez également spécifier d'autres propriétés de style comme fontWeight, fontStyle, etc. si nécessaire
                color = Color.White

                )
        )
        Button(
            onClick = {
                val intent = Intent(context, LogInActivity::class.java)
                context.startActivity(intent)  },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Log in"
            )
        }
        Button(
            onClick = {
                val intent = Intent(context, SignInActivity::class.java)
                context.startActivity(intent) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Sign in")
        }
    }

    // Bouton en bas à droite

}
@Preview(showBackground = true)
@Composable
fun AuthentificationScreenPreview() {
    ProjectGroupTheme {
       // AuthentificationScreen()
        // Greeting("Android")

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

