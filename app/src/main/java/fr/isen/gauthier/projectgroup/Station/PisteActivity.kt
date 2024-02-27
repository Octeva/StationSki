package fr.isen.gauthier.projectgroup.Station

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Network.PisteCategory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PisteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            test()
        }
    }
}

@Composable
fun GetData(categories: SnapshotStateList<PisteCategory>) {
    CallDataBase.database.getReference("pistes")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //val categories: MutableList<PisteCategory> = mutableListOf()
                snapshot.children.forEach {
                    val pistes = it.children.mapNotNull { it.getValue(Piste::class.java) }
                    categories.add(PisteCategory(it.key ?: "", pistes))
                }
                Log.d("database", snapshot.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("dataBase", error.toString())
            }
        })
}

@Composable
fun test() {
    val expandedCategoryIndex = remember { mutableStateOf<Int?>(null) }
    val categories = remember { mutableStateListOf<PisteCategory>() }

    LaunchedEffect(Unit) {
        val newData = getDataFromDatabase()
        categories.addAll(newData)
    }

    val colors = listOf(Color.Cyan, Color.Gray, Color.Red, Color.Green)

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
                        // Afficher les pistes de la catégorie
                        category.pistes.forEach { piste ->
                            Text(
                                text = piste.name, // Suppose que Piste a une propriété 'name'
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
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




//Modifier.clickable {  },