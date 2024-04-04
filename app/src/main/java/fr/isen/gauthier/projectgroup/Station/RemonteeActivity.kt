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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Network.Remontee
import fr.isen.gauthier.projectgroup.Network.RemonteeCategory
import fr.isen.gauthier.projectgroup.Network.getRemonteeEtatInDatabase
import fr.isen.gauthier.projectgroup.Station.DetailRemonteeActivity
import fr.isen.gauthier.projectgroup.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RemonteeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListeRemontee(RemonteeCategory())
        }
    }
}


@Composable
fun ListeRemontee(category: RemonteeCategory) {
    val context = LocalContext.current
    var expandedCategoryIndex = remember { mutableStateOf<Int?>(null) }
    val categories = remember { mutableStateListOf<RemonteeCategory>() }


    LaunchedEffect(Unit) {
        val newData = getDataDatabase()
        categories.addAll(newData)
    }
    val colors = listOf(Color.Cyan, Color.Red)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            val categoryColor = colors.getOrNull(index % colors.size) ?: Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = categoryColor)
                    .padding(16.dp)
                    .clickable {
                        expandedCategoryIndex.value = index
                    },
                contentAlignment = Alignment.Center
            ) {
                Column {
                    // Affiche le nom de la catégorie
                    Text(
                        text = category.codeR,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Si l'index de la catégorie est le même que l'index de la catégorie actuellement étendue
                    if (expandedCategoryIndex.value == index) {
                        // Afficher les boutons pour chaque remontee de la catégorie
                        category.remontee.forEach { remontee ->
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(context, DetailRemonteeActivity::class.java)
                                    intent.putExtra("remonteeName", remontee.name)
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                val etatRemontee = remember { mutableStateOf("Chargement...") }

                                LaunchedEffect(remontee) {
                                    getRemonteeEtatInDatabase(remontee) { etat ->
                                        etatRemontee.value = etat
                                    }
                                }

                                Text(
                                    text = "${remontee.name} : ${etatRemontee.value}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black

                                )
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



suspend fun getDataDatabase(): List<RemonteeCategory> {
    return suspendCoroutine { continuation ->
        CallDataBase.database.getReference("remontee")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newDatas = mutableListOf<RemonteeCategory>()
                    snapshot.children.forEach {categorySnapshot ->
                        val categoryName = categorySnapshot.key ?: ""
                        val remontee = categorySnapshot.children.mapNotNull { remonteeSnapshot ->
                            remonteeSnapshot.getValue(Remontee::class.java)
                        }
                        newDatas.add(RemonteeCategory(categoryName, remontee))
                    }
                    continuation.resume(newDatas)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(emptyList())
                }
            })
    }
}