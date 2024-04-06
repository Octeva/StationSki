package fr.isen.gauthier.projectgroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.app.Activity
import android.content.ContentValues.TAG
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import fr.isen.gauthier.projectgroup.Station.User
import fr.isen.gauthier.projectgroup.Station.WelcomeActivity
import fr.isen.gauthier.projectgroup.Station.getUserPseudo


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
        }
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
                    style = TextStyle(
                        fontFamily = FontFamily.Serif, // Changer la famille de police
                        fontSize = 40.sp, // Changer la taille de la police en sp (scaled pixels)
                        color = Color.White

                    )
                )
                // TextField pour saisir email
                TextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Adresse e-mail") },
                    modifier = Modifier.padding(16.dp)
                )
                // TextField pour saisir password
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
                            }
                        )
                    )
                }

                var AlreadyAccount = type == AuthenticationType.LOGIN // Variable pour suivre l'état de l'action attendue sur la page de connexion
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

                        Toast.makeText(context, "Saisie validée!", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = if (AlreadyAccount) "Connexion" else "Créer le compte")
                }
                Spacer(modifier = Modifier.height(90.dp))
                Button(
                    onClick = {
                        Toast.makeText(context, "Retour à l'accueil", Toast.LENGTH_LONG).show()
                        (context as? ComponentActivity)?.finish() // Terminer l'activité actuelle pour revenir à l'écran précédent (HomeActivity) }, // Finish the activity when the button is clicked
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Retour à la page principale")
                }
            }
        }
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
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("TAG", "already account like this in database : failure")

                } else {
                    // L'adresse e-mail n'est pas déjà utilisée, créer le compte
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { createTask ->
                            if (createTask.isSuccessful) {
                                val firebaseUser = auth.currentUser
                                if (firebaseUser != null){
                                    val userId = firebaseUser.uid
                                    val newUser = User(email = email, pseudo = pseudo)
                                    saveUserToDatabase(userId, newUser)
                                    updateProfileWithPseudo(pseudo, firebaseUser)
                                }
                                auth.currentUser?.updateProfile(userProfileChangeRequest {
                                    displayName = pseudo //ici lorsque le compte est créé, on connecte le pseudo avec l'identifiant qui a été créé sur Firebase, et après on veut que ça nous mène à la page Bienvenue
                                })
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success")
                                Toast.makeText(
                                    activity,
                                    "Compte créé avec succès!",
                                    Toast.LENGTH_LONG
                                ).show()
                                // Redirection vers l'activité de connexion
                                val intent = Intent(activity, LogInActivity::class.java)
                                Log.d("TAG", "go to login to put your credentials after signing up")
                                Toast.makeText(
                                    activity,
                                    "Connectez-vous avec vos coordonnées",
                                    Toast.LENGTH_LONG
                                ).show()
                                activity.startActivity(intent)
                                activity.finish() // Fin de l'activité actuelle pour empêcher l'utilisateur de revenir en arrière
                            } else {
                                // Échec de la création du compte
                                Log.w("TAG", "createUserWithEmail :failure", createTask.exception)

                                Toast.makeText(
                                    activity,
                                    "Erreur création du compte, veuillez réessayer.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Échec de la vérification de l'adresse e-mail
                Toast.makeText(
                    activity,
                    "Adresse mail déjà existante, veuillez en choisir une autre",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("TAG", "createUserWithEmail mail address already used in database : failure")


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
                    Log.d("TAG", "logInWithEmail:success")
                    Toast.makeText(activity, "Bien connecté.e!", Toast.LENGTH_LONG).show()
                    val user = auth.currentUser
                    val pseudo = user?.displayName
                    val intent = Intent(activity, WelcomeActivity::class.java)
                    intent.putExtra("pseudo",pseudo)
                    activity.startActivity(intent)


                } else {
                    //échec de la connexion
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "logInWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity,
                        "Adresse e-mail ou mot de passe incorrect.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}

private fun saveUserToDatabase(userId: String, user: User) {
    val database = FirebaseDatabase.getInstance().reference
    val userReference = database.child("users").child(userId)
    userReference.setValue(user)
}

private fun updateProfileWithPseudo(pseudo: String, firebaseUser: FirebaseUser) {
    val profileUpdates = userProfileChangeRequest {
        displayName = pseudo
    }
    firebaseUser.updateProfile(profileUpdates)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "User profile updated.")
            } else {
                Log.e("TAG", "Failed to update user profile.")
            }
        }
}