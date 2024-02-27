package fr.isen.gauthier.projectgroup


import android.app.Activity
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SignInActivity : EmailPasswordActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConnexionScreen(AuthenticationType.SIGNIN, auth)
        }
    }
}

/*
@Composable
fun SignInScreen() {
    val context = LocalContext.current

    // Remember the values entered by the user for email and password
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    // Image resource for background
    val backgroundImage = painterResource(R.drawable.skier___travers_des_paysages_magiques_aux_arcs)


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

        // TextField for entering email
        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Adresse e-mail") },
            modifier = Modifier.padding(16.dp)
        )

        // TextField for entering password
        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Mot de passe") },
            modifier = Modifier.padding(16.dp),
            visualTransformation = PasswordVisualTransformation(), // Hide password characters
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // You can perform any action here when the user presses the Done button on the keyboard
                }
            )
        )

        var AlreadyAccount = type == AuthenticationType.SIGNIN // Variable pour suivre l'état de l'action attendue sur la page de connexion

        Button(
            onClick = {
                val email = emailState.value
                val password = passwordState.value

                if (AlreadyAccount) {
                    signIn(email, password, auth, context as Activity)
                    //sendEmailVerification(auth, context as Activity)
                } else {
                    createAccount(email, password, auth, context as Activity)
                    // Autres actions à exécuter après la création de compte
                }

                Toast.makeText(context, "Saisie validée!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = if (AlreadyAccount) "Connexion" else "Créer le compte")
        }

        Spacer(modifier = Modifier.height(90.dp))


        Button(
            onClick = {
                Toast.makeText(context, "Retour à l'accueil", Toast.LENGTH_SHORT).show()
                (context as? ComponentActivity)?.finish() // Terminer l'activité actuelle pour revenir à l'écran précédent (HomeActivity) }, // Finish the activity when the button is clicked
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Retour à la page principale")
        }

    }



    }


}

 */