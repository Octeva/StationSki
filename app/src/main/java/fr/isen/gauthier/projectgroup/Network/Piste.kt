package fr.isen.gauthier.projectgroup.Network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Station.getPisteCategoryAndIndexByName
import java.io.Serializable
data class Piste (

    var affluence : Int = 0,
    var cross: List<Cross> = listOf(),
    var etat : Boolean = true,
    var name: String = " ",
    var visibility : Int = 0
): Serializable

class PisteCategory (
    var code: String = "",
    var pistes: List<Piste> = listOf()
)

fun getPisteEtat(piste: Piste): String {
    if (piste.etat){
        return "Ouverte"
    }
    return "Fermée"
}

suspend fun getPisteEtatInDatabase(piste: Piste, onEtatChanged: (String) -> Unit) {
    val (pisteCategory, pisteIndex) = getPisteCategoryAndIndexByName(piste.name) ?: return
    val pisteRef = CallDataBase.database.getReference("pistes/${pisteCategory}/${pisteIndex}/etat")
    pisteRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val etat = snapshot.getValue(Boolean::class.java) ?: return
            // Convertissez l'état boolean en String ("Ouverte" ou "Fermée") et utilisez onEtatChanged pour mettre à jour l'UI
            onEtatChanged(if (etat) "Ouverte" else "Fermée")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Piste", "Failed to read value.", error.toException())
        }
    })
}

suspend fun setPisteEtat(piste: Piste, etat: Boolean) {
    val pisteCategoryAndIndex = getPisteCategoryAndIndexByName(piste.name)
    if (pisteCategoryAndIndex != null) {
        val (pisteCategory, pisteIndex) = pisteCategoryAndIndex
        piste.etat = etat
        val pisteRef = CallDataBase.database.getReference("pistes").child(pisteCategory).child(pisteIndex.toString())
        pisteRef.setValue(piste)
    } else {
        Log.e("Piste", "Piste not found")
    }
}

fun getPisteAffluence(piste: Piste): String {
    if (piste.affluence == 0){
        return "peu fréquentée"
    }
    else if (piste.affluence == 1){
        return "moyennement fréquentée"
    }
    return "très fréquentée"
}

suspend fun getPisteAffluenceInDatabase(piste: Piste, onAffluenceChanged: (String) -> Unit) {
    val (pisteCategory, pisteIndex) = getPisteCategoryAndIndexByName(piste.name) ?: return
    val pisteRef = CallDataBase.database.getReference("pistes/${pisteCategory}/${pisteIndex}/affluence")
    pisteRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val affluence = snapshot.getValue(Int::class.java) ?: return
            onAffluenceChanged(if (affluence == 0) "peu fréquentée" else if (affluence == 1) "moyennement fréquentée" else "très fréquentée")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Piste", "Failed to read value.", error.toException())
        }
    })
}

suspend fun setPisteAffluence(piste: Piste, affluence: Int) {
    val pisteCategoryAndIndex = getPisteCategoryAndIndexByName(piste.name)
    if (pisteCategoryAndIndex != null) {
        val (pisteCategory, pisteIndex) = pisteCategoryAndIndex
        piste.affluence = affluence
        val pisteRef = CallDataBase.database.getReference("pistes").child(pisteCategory).child(pisteIndex.toString())
        pisteRef.setValue(piste)
    } else {
        Log.e("Piste", "Piste not found")
    }
}


fun getPisteMeteo(piste: Piste): String {
    if (piste.visibility == 0){
        return "Soleil"
    }
    else if (piste.visibility == 1){
        return "nuageux"
    }
    else if (piste.visibility == 2){
        return "brouillard"
    }
    else if (piste.visibility == 3){
        return "neige"
    }
    else if (piste.visibility == 4){
        return "vent"
    }
    return "pluie"
}

suspend fun getPisteMeteoInDatabase(piste: Piste, onMeteoChanged: (String) -> Unit) {
    val (pisteCategory, pisteIndex) = getPisteCategoryAndIndexByName(piste.name) ?: return
    val pisteRef = CallDataBase.database.getReference("pistes/${pisteCategory}/${pisteIndex}/visibility")
    pisteRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val visibility = snapshot.getValue(Int::class.java) ?: return
            onMeteoChanged(if (visibility == 0) "peu fréquentée" else if (visibility == 1) "moyennement fréquentée" else "très fréquentée")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Piste", "Failed to read value.", error.toException())
        }
    })
}

suspend fun setPisteMeteo(piste: Piste, visibility: Int) {
    val pisteCategoryAndIndex = getPisteCategoryAndIndexByName(piste.name)
    if (pisteCategoryAndIndex != null) {
        val (pisteCategory, pisteIndex) = pisteCategoryAndIndex
        piste.visibility = visibility
        val pisteRef = CallDataBase.database.getReference("pistes").child(pisteCategory).child(pisteIndex.toString())
        pisteRef.setValue(piste)
    } else {
        Log.e("Piste", "Piste not found")
    }
}