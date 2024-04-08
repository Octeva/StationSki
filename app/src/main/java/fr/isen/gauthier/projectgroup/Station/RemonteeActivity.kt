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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Network.Remontee
import fr.isen.gauthier.projectgroup.Network.RemonteeCategory
import fr.isen.gauthier.projectgroup.Network.getRemonteeEtat
import fr.isen.gauthier.projectgroup.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RemonteeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val etatRemontee = remember { mutableStateOf(getRemonteeEtat(Remontee())) }
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

                        // Affiche le nom de la catégorie
                        Text(
                            text = category.codeR,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    // Si l'index de la catégorie est le même que l'index de la catégorie actuellement étendue
                    if (expandedCategoryIndex.value == index) {
                        val openRemontee = category.remontee.filter { it.etat }
                        val closedRemontee = category.remontee.filter { !it.etat }
                        val sortedRemontee = openRemontee + closedRemontee

                        // Afficher les boutons pour chaque remontee de la catégorie
                        sortedRemontee.forEach { remontee ->
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
                                Row (
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        text = remontee.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                    Image(
                                        painterResource(if (remontee.etat) R.drawable.pastille_verte else R.drawable.pastille_rouge),
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