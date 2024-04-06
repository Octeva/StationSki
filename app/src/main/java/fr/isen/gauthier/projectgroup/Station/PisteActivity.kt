package fr.isen.gauthier.projectgroup.Station

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Network.PisteCategory
import fr.isen.gauthier.projectgroup.Network.getPisteEtat
import fr.isen.gauthier.projectgroup.Network.getPisteEtatInDatabase
import fr.isen.gauthier.projectgroup.R
import fr.isen.gauthier.projectgroup.Station.DetailPisteActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.compose.foundation.layout.Row


class PisteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            val etatPiste = remember { mutableStateOf(getPisteEtat(Piste())) }
            ListePiste(PisteCategory())
        }
    }
}

@Composable
fun ListePiste(category: PisteCategory) {
    val expandedCategoryIndex = remember { mutableStateOf<Int?>(null) }
    val categories = remember { mutableStateListOf<PisteCategory>() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val newData = getDataFromDatabase()
        categories.addAll(newData)
    }

    val colors = listOf(Color.Cyan, Color.Gray, Color.Red, Color.Green)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(categories) { index, category ->
                val categoryColor = colors.getOrNull(index % colors.size) ?: Color.Transparent

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Affiche le nom de la catégorie avec le fond coloré
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(color = categoryColor)
                            .clickable {
                                expandedCategoryIndex.value = index
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.code,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Afficher les boutons pour chaque piste de la catégorie
                    if (expandedCategoryIndex.value == index) {
                        val openPistes = category.pistes.filter { it.etat }
                        val closedPistes = category.pistes.filter { !it.etat }
                        val sortedPistes = openPistes + closedPistes

                        sortedPistes.forEach { piste ->
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(context, DetailPisteActivity::class.java)
                                    intent.putExtra("pisteName", piste.name)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = piste.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                    Image(
                                        painterResource(if (piste.etat) R.drawable.aaaa else R.drawable.bbbbb),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }





    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
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
                    painterResource(R.drawable._200px_alpine_skiing_pictogram_svg),
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
                    painterResource(R.drawable._205016),
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
                    painterResource(R.drawable.picto_maison_png),
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
                    painterResource(R.drawable._58656_chat_icon_free_clipart_hd),
                    contentDescription = null
                )
            }
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "rafraiche", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(R.drawable.refresh_icon),
                    contentDescription = null
                )
            }
        }
    }
}



suspend fun getDataFromDatabase(): List<PisteCategory> {
    return suspendCoroutine { continuation ->
        CallDataBase.database.getReference("pistes")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newData = mutableListOf<PisteCategory>()
                    snapshot.children.forEach { categorySnapshot ->
                        val categoryName = categorySnapshot.key ?: ""
                        val pistes = categorySnapshot.children.mapNotNull { pisteSnapshot ->
                            pisteSnapshot.getValue(Piste::class.java)
                        }
                        newData.add(PisteCategory(categoryName, pistes))
                    }
                    continuation.resume(newData)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    continuation.resume(emptyList())
                }
            })
    }
}
