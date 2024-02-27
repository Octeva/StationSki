package fr.isen.gauthier.projectgroup

//import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LogInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LogInScreen()

        }
    }
}


@Composable
fun LogInScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Log In",
            //style = MaterialTheme.typography.h5
            style = TextStyle(
                fontFamily = FontFamily.Serif, // Changer la famille de police
                fontSize = 40.sp, // Changer la taille de la police en sp (scaled pixels)
                // Vous pouvez également spécifier d'autres propriétés de style comme fontWeight, fontStyle, etc. si nécessaire
                color = Color.White

            )
        )
        Button(
            onClick = { /* TODO: Action when Login button is clicked */ },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Identifiant"
            )
        }
        Button(
            onClick = { /* TODO: Action when Sign in button is clicked */ },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Mot de passe")
        }

        Spacer(modifier = Modifier.height(90.dp))


        Button(
            onClick = {
                Toast.makeText(context, "Retour à l'accueil", Toast.LENGTH_SHORT).show()
                (context as? ComponentActivity)?.finish() // Terminer l'activité actuelle pour revenir à l'écran précédent (HomeActivity) }, // Finish the activity when the button is clicked
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Retour")
        }

    }


}