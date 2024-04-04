package fr.isen.gauthier.projectgroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.app.Activity
import android.content.Intent
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
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import fr.isen.gauthier.projectgroup.Station.WelcomeActivity


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

        if (auth.currentUser != null) {
            // L'utilisateur est déjà connecté
            //redirectToWelcomeActivity(this)
        } else {
            // L'utilisateur n'est pas connecté, démarrer l'écran d'authentification
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish() // Optionnel : fermer cette activité pour empêcher l'utilisateur de revenir en arrière
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Utilisateur déjà connecté, rediriger vers l'écran d'accueil
            //redirectToWelcomeActivity(this)
        }
    }

    override fun onStop() {
        super.onStop()
        // Déconnexion de l'utilisateur lors de la fermeture de l'application
        FirebaseAuth.getInstance().signOut()
    }
    @Composable
    fun ConnexionScreen(
        type: AuthenticationType,
        auth: FirebaseAuth,
        activity: Activity
    ) {
        val context = LocalContext.current
        // Remember the values entered by the user for email, password and pseudo of the user
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        val pseudoState = remember { mutableStateOf("") }

        // Image resource for background
        val backgroundImage =
            painterResource(R.drawable.skier___travers_des_paysages_magiques_aux_arcs)
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
                if (type == AuthenticationType.SIGNIN) { //on teste si l'utilisateur clique sur sign up -> alors on met le pseudo
                    TextField(
                        value = pseudoState.value,
                        onValueChange = { pseudoState.value = it },
                        label = { Text("Pseudo") },
                        modifier = Modifier.padding(16.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // You can perform any action here when the user presses the Done button on the keyboard
                            }
                        )
                    )
                }

                var AlreadyAccount =
                    type == AuthenticationType.LOGIN // Variable pour suivre l'état de l'action attendue sur la page de connexion
                Button(
                    onClick = {
                        val email = emailState.value
                        val password = passwordState.value
                        val pseudo = pseudoState.value

                        if (AlreadyAccount) {
                            signIn(email, password, auth, context as Activity)
                            //sendEmailVerification(auth, context as Activity)
                        } else {
                            createAccount(email, password, pseudo, auth, context as Activity)
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
    //private fun redirectToWelcomeActivity(activity: Activity) {
      //  val intent = Intent(activity, WelcomeActivity::class.java)
        //activity.startActivity(intent)
        //activity.finish() // Fermer l'activité actuelle pour revenir à l'écran précédent
    //}


}




private fun createAccount(
    email: String,
    password: String,
    pseudo: String,
    auth: FirebaseAuth,
    activity: Activity
) {
    // [START create_user_with_email]

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { signInTask ->
            if (signInTask.isSuccessful) {
                    // L'adresse e-mail est déjà utilisée
                    Toast.makeText(
                        activity,
                        "Cette adresse e-mail est déjà utilisée.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // L'adresse e-mail n'est pas déjà utilisée, créer le compte
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { createTask ->
                            if (createTask.isSuccessful) {
                                auth.currentUser?.updateProfile(userProfileChangeRequest {
                                    displayName =
                                        pseudo //ici lorsque le compte est créé, on connecte le pseudo avec l'identifiant qui a été créé sur firebase, et après on veut que ça nous mène à la page Bienvenue
                                })
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success")
                                Toast.makeText(
                                    activity,
                                    "Compte créé avec succès!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Échec de la création du compte
                                Log.w("TAG", "createUserWithEmail:failure", createTask.exception)

                                Toast.makeText(
                                    activity,
                                    "Erreur lors de la création du compte. Veuillez réessayer.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Échec de la vérification de l'adresse e-mail
                Toast.makeText(
                    activity,
                    "Erreur lors de la vérification de l'adresse e-mail. Veuillez réessayer.",
                    Toast.LENGTH_SHORT
                ).show()
            }

}



private fun signIn(
    email: String,
    password: String,
    auth: FirebaseAuth,
    activity: Activity
) {
    // [START sign_in_with_email]
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                //Connexion réussie
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithEmail:success")
                Toast.makeText(activity, "Bien connecté!", Toast.LENGTH_SHORT).show()
               // redirectToWelcomeActivity(activity)
                val user = auth.currentUser

            } else {
                //échec de la connexion
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithEmail:failure", task.exception)
                Toast.makeText(activity, "Adresse e-mail ou mot de passe incorrect.", Toast.LENGTH_SHORT).show()
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




//private fun redirectToHome(activity: Activity) {
  //  val intent = Intent(activity, WelcomeActivity::class.java)
    //activity.startActivity(intent)
    //activity.finish() // Facultatif : pour fermer l'activité actuelle
//}

//private fun saveLoginState(context: Context, isLoggedIn: Boolean) {
  //  val sharedPref =
    //    context.getSharedPreferences("login_state", android.content.Context.MODE_PRIVATE)
    //with(sharedPref.edit()) {
      //  putBoolean("isLoggedIn", isLoggedIn)
        //apply()
    //}
//}




