package fr.isen.gauthier.projectgroup.Station

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Network.Piste
import fr.isen.gauthier.projectgroup.Network.PisteCategory

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

@Composable fun test() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var categories = remember {
        mutableStateListOf<PisteCategory>()
    }

    LazyColumn {
        items(categories) {
            Text(it.code)
            if(true) {
                it.pistes.forEach {
                    Text(it.name)
                }
            }
        }
    }

    GetData(categories)
}

//Modifier.clickable {  },