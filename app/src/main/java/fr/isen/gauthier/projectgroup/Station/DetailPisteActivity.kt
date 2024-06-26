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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
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
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Network.Remontee
//import fr.isen.gauthier.projectgroup.Network.SaveDetailPiste
import fr.isen.gauthier.projectgroup.Network.setPisteAffluence
import fr.isen.gauthier.projectgroup.Network.setPisteEtat
import fr.isen.gauthier.projectgroup.Network.setPisteMeteo
import fr.isen.gauthier.projectgroup.R
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class DetailPisteActivity : ComponentActivity() {

//    companion object {
//        val DETAIL_EXTRA_KEY = "DETAIL_EXTRA_KEY"
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pisteName = intent.getSerializableExtra("pisteName") as String

        val pseudo = intent.getStringExtra("pseudo") ?: ""
        Log.d("DetailRemonteeActivity", "pseudo: $pseudo")

            lifecycleScope.launch {
                val piste = getPisteByName(pisteName)
            setContent {


                Surface {
                    ScaffoldPiste(piste = piste!!, pseudo = "$pseudo")
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldPiste(piste: Piste, pseudo: String){
var context = LocalContext.current
val etatPiste = remember { mutableStateOf(piste?.etat ?: false) }
val affluencePiste = remember { mutableStateOf(piste?.affluence ?: 0) }
val meteoPiste = remember { mutableStateOf(piste?.visibility ?: 0) }

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
                        piste.name,
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
            piste?.let { nonNullPiste->
                // Appel de fonctions avec nonNullRemontee qui est garanti de ne pas être nul
                DetailPiste(
                    piste = nonNullPiste,
                    etatPiste = etatPiste,
                    affluencePiste = affluencePiste,
                    meteoPiste = meteoPiste
                )
                ModifierPiste(
                    piste = nonNullPiste,
                    etatPiste = etatPiste,
                    affluencePiste = affluencePiste,
                    meteoPiste = meteoPiste
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
fun DetailPiste(piste: Piste, etatPiste: MutableState<Boolean>, affluencePiste: MutableState<Int>, meteoPiste: MutableState<Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {


        Row{
            Text(
                text = "Etat de la piste :",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
            )
            val imageEtat: Painter = when (etatPiste.value) {
                true -> painterResource(id = R.drawable.drapeau_vert)
                else -> painterResource(id = R.drawable.drapeau_rouge)
            }
            Image(
                painter = imageEtat,
                contentDescription = null, // Ajustez cette valeur si nécessaire
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(50.dp),
            )
        }

        Row{
            Text(
                text = "Affluence sur la piste :",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
            )
            val imageAffluence: Painter = when (affluencePiste.value) {
                0 -> painterResource(id = R.drawable.peu) // Image pour "soleil"
                1 -> painterResource(id = R.drawable.moyen) // Image pour "nuageux"
                else -> painterResource(id = R.drawable.beaucoup) // Image pour "brouillard"
            }
            Image(
                painter = imageAffluence,
                contentDescription = null, // Ajustez cette valeur si nécessaire
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(50.dp),
            )
        }
        Row {
            Text(
                text = "Météo sur la piste :",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
            )
            val imageMeteo: Painter = when (meteoPiste.value) {
                0 -> painterResource(id = R.drawable.soleil) // Image pour "soleil"
                1 -> painterResource(id = R.drawable.nuage) // Image pour "nuageux"
                2 -> painterResource(id = R.drawable.brouillard) // Image pour "brouillard"
                3 -> painterResource(id = R.drawable.flocon_de_neige) // Image pour "neige"
                4 -> painterResource(id = R.drawable.venteux) // Image pour "venteux"
                else -> painterResource(id = R.drawable.pluie) // Image par défaut pour "pluie"
            }
            Image(
                painter = imageMeteo,
                contentDescription = null, // Ajustez cette valeur si nécessaire
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(50.dp),
            )
        }
    }
}

@Composable
fun ModifierPiste(piste: Piste, etatPiste: MutableState<Boolean>, affluencePiste: MutableState<Int>, meteoPiste: MutableState<Int>) {
    val coroutineScope = rememberCoroutineScope()
    val coroutineScope2 = rememberCoroutineScope()
    val coroutineScope3 = rememberCoroutineScope()

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
                        setPisteEtat(piste, true)
                        etatPiste.value = true
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
                        setPisteEtat(piste, false)
                        etatPiste.value = false
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
            text = "Quel est l'affluence sur la piste ?",
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier
                .padding(top = 15.dp, end = 5.dp, start = 15.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 15.dp, end = 5.dp, start = 5.dp)
                .fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = {
                    coroutineScope2.launch {
                        setPisteAffluence(piste, 0)
                        affluencePiste.value = 0
                    }
                          }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.peu),
                    contentDescription = "peu"
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope2.launch {
                        setPisteAffluence(piste, 1)
                        affluencePiste.value = 1
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.moyen),
                    contentDescription = "moyen"
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope2.launch {
                        setPisteAffluence(piste, 2)
                        affluencePiste.value = 2
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.beaucoup),
                    contentDescription = "beaucoup"
                )
            }
        }


        Text(
            text = "Quel est la météo sur la piste ?",
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
                    coroutineScope3.launch {
                        setPisteMeteo(piste, 0)
                        meteoPiste.value = 0
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.soleil),
                    contentDescription = "soleil"
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope3.launch {
                        setPisteMeteo(piste, 1)
                        meteoPiste.value = 1
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.nuage),
                    contentDescription = "nuage"
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope3.launch {
                        setPisteMeteo(piste, 2)
                        meteoPiste.value = 2
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.brouillard),
                    contentDescription = "brouillard"
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
                onClick = { coroutineScope3.launch {
                    setPisteMeteo(piste, 3)
                    meteoPiste.value = 3
                } }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.flocon_de_neige),
                    contentDescription = "flocon"
                )
            }
            OutlinedButton(
                onClick = { coroutineScope3.launch {
                    setPisteMeteo(piste, 4)
                    meteoPiste.value = 4
                } }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.venteux),
                    contentDescription = "venteux"
                )
            }
            OutlinedButton(
                onClick = {
                    coroutineScope3.launch {
                        setPisteMeteo(piste, 5)
                        meteoPiste.value = 5
                    }
                }, modifier = Modifier
                    .height(35.dp)
                    .padding(start = 15.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.pluie),
                    contentDescription = "pluie"
                )
            }
        }

        Button(
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 10.dp, start = 10.dp)
        ) {
            Text(text = "Valider les modifications")
        }
    }
}


suspend fun getPisteByName(pisteName: String): Piste? {
    return suspendCoroutine { continuation ->
        CallDataBase.database.getReference("pistes")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var piste: Piste? = null
                    snapshot.children.forEach { categorySnapshot ->
                        categorySnapshot.children.forEach { pisteSnapshot ->
                            val currentPiste = pisteSnapshot.getValue(Piste::class.java)
                            if (currentPiste?.name == pisteName) {
                                piste = currentPiste
                                return@forEach
                            }
                        }
                    }
                    continuation.resume(piste)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    continuation.resume(null)
                }
            })
    }
}

suspend fun getPisteCategoryAndIndexByName(pisteName: String): Pair<String, Int>? {
    return suspendCoroutine { continuation ->
        CallDataBase.database.getReference("pistes")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var result: Pair<String, Int>? = null
                    snapshot.children.forEach { categorySnapshot ->
                        categorySnapshot.children.forEachIndexed { index, pisteSnapshot ->
                            val currentPiste = pisteSnapshot.getValue(Piste::class.java)
                            if (currentPiste?.name == pisteName) {
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