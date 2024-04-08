package fr.isen.gauthier.projectgroup.Station

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Network.Remontee
import fr.isen.gauthier.projectgroup.Network.setRemonteeEtat
import fr.isen.gauthier.projectgroup.Network.setRemonteeWaiting
import fr.isen.gauthier.projectgroup.R
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class DetailRemonteeActivity : ComponentActivity() {

//    companion object {
//        val DETAIL_EXTRA_KEY = "DETAIL_EXTRA_KEY"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val remonteeName = intent.getSerializableExtra("remonteeName") as String
        val pseudo = intent.getStringExtra("pseudo") ?: ""
        Log.d("DetailRemonteeActivity", "pseudo: $pseudo")

        lifecycleScope.launch {
            val remontee = getRemonteeByName(remonteeName)

            setContent {

                ScaffoldRemontee(remontee = remontee!!, pseudo = pseudo)
            }

        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldRemontee(remontee: Remontee, pseudo: String) {
    var presses by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val etatRemontee = remember { mutableStateOf(remontee?.etat ?: false) }
    val waitingRemontee = remember { mutableStateOf(remontee?.waiting ?: 0) }

    // Définissez une liste mutable pour stocker les commentaires
    val commentaires = remember { mutableStateListOf<String>() }
    // Définissez un état pour stocker le commentaire entré par l'utilisateur
    val commentaireText = remember { mutableStateOf("") }

    // Fonction pour ajouter un commentaire
    val ajouterCommentaire: () -> Unit = {
        val commentaire = commentaireText.value.trim()
        if (commentaire.isNotBlank()) {
            commentaires.add(commentaire)
            // Effacer le champ de texte après l'ajout du commentaire
            commentaireText.value = ""
        }
    }
    Log.d("DetailRemonteeActivity", "Pseudo: $pseudo")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        remontee.name,
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 8.dp)
                    .background(color = colorResource(id = R.color.purple_500))
            ) {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, PisteActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painterResource(R.drawable.pictogramme_ski),
                        contentDescription = null
                    )
                }
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, RemonteeActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painterResource(R.drawable.pictogramme_telecabine),
                        contentDescription = null
                    )
                }
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, WelcomeActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painterResource(R.drawable.pictogramme_maison),
                        contentDescription = null
                    )
                }
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "blabla", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painterResource(R.drawable.pictogramme_conversation),
                        contentDescription = null
                    )
                }
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "refresh", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painterResource(R.drawable.refresh_icon),
                        contentDescription = null
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()), // Ajoutez un défilement vertical à la colonne
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            //Fonction Pour afficher les details de la piste entrer par les utilisateurs
            remontee?.let { nonNullRemontee ->
                // Appel de fonctions avec nonNullRemontee qui est garanti de ne pas être nul
                DetailRemontee(
                    remontee = nonNullRemontee,
                    etatRemontee = etatRemontee,
                    waitingRemontee = waitingRemontee
                )
                ModifierRemontee(
                    remontee = nonNullRemontee,
                    etatRemontee = etatRemontee,
                    waitingRemontee = waitingRemontee
                )
            }

            // Champ de texte pour ajouter un commentaire
            OutlinedTextField(
                value = commentaireText.value,
                onValueChange = { commentaireText.value = it },
                label = { Text("Ajouter un commentaire") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Bouton pour ajouter un commentaire
            Button(
                onClick = ajouterCommentaire,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, top = 8.dp)
            ) {
                Text("Ajouter")
            }


                // Liste des commentaires
                commentaires.forEach { commentaire ->
                    Card {
                    Text(
                        text = commentaire,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = TextStyle(fontStyle = FontStyle.Italic)
                    )
                        Text(text = "Commentaire de $pseudo")
                }
            }

        }
    }
}

@Composable
fun DetailRemontee(
    remontee: Remontee,
    etatRemontee: MutableState<Boolean>?,
    waitingRemontee: MutableState<Int>?
) {
    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {


        Row {
            Text(
                text = "Etat de la remontee :",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
            )
            val imageEtatRemontee: Painter = if (etatRemontee?.value == true) {
                painterResource(id = R.drawable.drapeau_vert)
            } else {
                painterResource(id = R.drawable.drapeau_rouge)
            }
            Image(
                painter = imageEtatRemontee,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(50.dp),
            )
        }

        Row {
            Text(
                text = "Temps d'attente à la remontée : ",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
            )
            val imageWaiting: Painter = when (waitingRemontee?.value) {
                0 -> painterResource(id = R.drawable.min5) // Image pour "soleil"
                1 -> painterResource(id = R.drawable.min10) // Image pour "nuageux"
                2 -> painterResource(id = R.drawable.min15) // Image pour "brouillard"
                3 -> painterResource(id = R.drawable.min20) // Image pour "neige"
                else -> painterResource(id = R.drawable.min25) // Image pour "venteux"
            }
            Image(
                painter = imageWaiting,
                contentDescription = null, // Ajustez cette valeur si nécessaire
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(50.dp),
            )
        }
        Text(
            text = "Pistes à la fin de la remontée :",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(top = 10.dp)
        )
        remontee.endRemontee.first().startPiste.forEach {
            Text(
                text = "- $it",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun ModifierRemontee(
    remontee: Remontee,
    etatRemontee: MutableState<Boolean>,
    waitingRemontee: MutableState<Int>
) {
    val coroutineScope = rememberCoroutineScope()
    val coroutineScope1 = rememberCoroutineScope()

    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text(
                text = "Les informations sur la piste ne sont plus à jour ?",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "Modifier les !",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Quel est l'état de la piste ?",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .padding(top = 15.dp, end = 5.dp, start = 15.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, end = 5.dp, start = 5.dp)
        ) {

            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        setRemonteeEtat(remontee, true)
                        etatRemontee.value = true
                    }
                },
                modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {

                Image(
                    painterResource(id = R.drawable.drapeau_vert),
                    contentDescription = "ouvert"
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        setRemonteeEtat(remontee, false)
                        etatRemontee.value = false
                    }
                },
                modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {

                Image(
                    painterResource(id = R.drawable.drapeau_rouge),
                    contentDescription = "ferme"
                )
            }

        }

        Text(
            text = "Quel est le temps d'attente à la remontée ?",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .padding(top = 15.dp, end = 5.dp, start = 15.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, end = 5.dp, start = 5.dp)
        ) {

            OutlinedButton(
                onClick = {
                    coroutineScope1.launch {
                        setRemonteeWaiting(remontee, 0)
                        waitingRemontee.value = 0
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "5 min",
                    color = colorResource(id = R.color.black)
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope1.launch {
                        setRemonteeWaiting(remontee, 1)
                        waitingRemontee.value = 1
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "10 min",
                    color = colorResource(id = R.color.black)
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope1.launch {
                        setRemonteeWaiting(remontee, 2)
                        waitingRemontee.value = 2
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "15 min",
                    color = colorResource(id = R.color.black)
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, end = 5.dp, start = 5.dp)
        ) {

            OutlinedButton(
                onClick = {
                    coroutineScope1.launch {
                        setRemonteeWaiting(remontee, 3)
                        waitingRemontee.value = 3
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "20 min",
                    color = colorResource(id = R.color.black)
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope1.launch {
                        setRemonteeWaiting(remontee, 4)
                        waitingRemontee.value = 4
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = "25 min",
                    color = colorResource(id = R.color.black)
                )
            }
        }

    }
}


suspend fun getRemonteeByName(remonteeName: String): Remontee? {
    return suspendCoroutine { continuation ->
        CallDataBase.database.getReference("remontee")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var remontee: Remontee? = null
                    snapshot.children.forEach { categorySnapshot ->
                        categorySnapshot.children.forEach { pisteSnapshot ->
                            val currentRemontee = pisteSnapshot.getValue(Remontee::class.java)
                            if (currentRemontee?.name == remonteeName) {
                                remontee = currentRemontee
                                return@forEach
                            }
                        }
                    }
                    continuation.resume(remontee)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    continuation.resume(null)
                }
            })
    }
}

suspend fun getRemonteeCategoryAndIndexByName(remonteeName: String): Pair<String, Int>? {
    return suspendCoroutine { continuation ->
        CallDataBase.database.getReference("remontee")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var result: Pair<String, Int>? = null
                    snapshot.children.forEach { categorySnapshot ->
                        categorySnapshot.children.forEachIndexed { index, remonteeSnapshot ->
                            val currentRemontee = remonteeSnapshot.getValue(Remontee::class.java)
                            if (currentRemontee?.name == remonteeName) {
                                result = Pair(categorySnapshot.key ?: "", index)
                                return@forEachIndexed
                            }
                        }
                    }
                    continuation.resume(result)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    continuation.resume(null)
                }
            })
    }
}