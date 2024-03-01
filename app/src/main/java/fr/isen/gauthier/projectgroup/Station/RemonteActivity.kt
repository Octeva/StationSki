package fr.isen.gauthier.projectgroup.Station


import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Network.PisteCategory
import fr.isen.gauthier.projectgroup.Network.RemonteCategory
import fr.isen.gauthier.projectgroup.Network.Remontee
import fr.isen.gauthier.projectgroup.R
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RemonteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            teste()
        }
    }
}

/*
@Composable
fun GetDataX(categories: SnapshotStateList<RemonteCategory>) {
    CallDataBase.database.getReference("remontee")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val remontee = it.children.mapNotNull { it.getValue(Remontee::class.java) }
                    categories.add(RemonteCategory(it.key?:"",remontee))
                }
                Log.d("database", snapshot.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("dataBase", error.toString())
            }
        })
}
*/

@Composable
fun teste() {
    val context = LocalContext.current
    var expandedCategoryIndex = remember { mutableStateOf<Int?>(null) }
    val categories = remember { mutableStateListOf<RemonteCategory>() }


    LaunchedEffect(Unit) {
        val newDatas = getDataDatabase()
        categories.addAll(newDatas)
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
                        text = category.code,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Si l'index de la catégorie est le même que l'index de la catégorie actuellement étendue
                    if (expandedCategoryIndex.value == index) {
                        // Afficher les boutons pour chaque piste de la catégorie
                        category.remontee.forEach { piste ->
                            OutlinedButton(
                                onClick = {
                                    // Afficher le Toast lorsque le bouton est cliqué
                                    Toast.makeText(
                                        context,
                                        "Vous voulez aller à ${piste.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    text = piste.name,
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
                    val intent = Intent(context, RemonteActivity::class.java)
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
                    val intent = Intent(context, Bienvenu::class.java)
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



suspend fun getDataDatabase(): List<RemonteCategory> {
    return suspendCoroutine { continuation ->
        CallDataBase.database.getReference("remontee")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newDatas = mutableListOf<RemonteCategory>()
                    snapshot.children.forEach {categorySnapshot ->
                        val categoryName = categorySnapshot.key ?: ""
                        val remontee = categorySnapshot.children.mapNotNull { remonteeSnapshot ->
                            remonteeSnapshot.getValue(Remontee::class.java)
                        }
                        newDatas.add(RemonteCategory(categoryName, remontee))
                    }
                    continuation.resume(newDatas)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(emptyList())
                }
            })
    }
}
