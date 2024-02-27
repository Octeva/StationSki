package fr.isen.gauthier.projectgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.components.Component
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



enum class AuthenticationType {
    LOGIN, SIGNIN;

    fun title(): String {
        return when(this) {
            LOGIN -> "Log in"
            SIGNIN -> "Sign up"
        }
    }
}
open class EmailPasswordActivity : ComponentActivity() {
    lateinit var auth: FirebaseAuth

    companion object {
        private const val TAG = "EmailPassword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        if(auth.currentUser != null) {
            // Connecté
        }



    }
}

@Composable
fun ConnexionScreen(type: AuthenticationType, auth: FirebaseAuth) {
    val context = LocalContext.current

    // Remember the values entered by the user for email and password
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    // Image resource for background
    val backgroundImage = painterResource(R.drawable.skier___travers_des_paysages_magiques_aux_arcs)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = backgroundImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )


        // Content
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = type.title(),
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

            var AlreadyAccount = type == AuthenticationType.LOGIN // Variable pour suivre l'état de l'action attendue sur la page de connexion

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



private fun createAccount(email: String, password: String, auth: FirebaseAuth, activity: Activity) {

    // [START create_user_with_email]
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "createUserWithEmail:success")
                val user = auth.currentUser
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    activity,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    // [END create_user_with_email]
}

private fun signIn(email: String, password: String, auth: FirebaseAuth, activity: Activity) {
    // [START sign_in_with_email]
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithEmail:success")
                val user = auth.currentUser

            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    activity,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    // [END sign_in_with_email]
}

private fun sendEmailVerification(auth: FirebaseAuth, activity: Activity) {
    // [START send_email_verification]
    val user = auth.currentUser
    user?.sendEmailVerification()
        ?.addOnCompleteListener(activity) { task ->
            // Email Verification sent
        }
    // [END send_email_verification]
}

//private fun updateUI(user: FirebaseUser?, auth: FirebaseAuth) {
//}









