package fr.isen.gauthier.projectgroup.Network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Station.getRemonteeCategoryAndIndexByName
import java.io.Serializable
data class Remontee (

    var waiting: Int = 0,
    var endRemontee: List<EndRemontee> = listOf(),
    var startRemontee: List<StartRemontee> = listOf(),
    var etat : Boolean = true,
    var name: String = " ",
): Serializable

class RemonteeCategory (
    var codeR: String = "",
    var remontee: List<Remontee> = listOf()
)

fun getRemonteeEtat(remontee: Remontee): String {
    if (remontee.etat){
        return "Ouverte"
    }
    return "Fermée"
}

suspend fun getRemonteeEtatInDatabase(remontee: Remontee, onEtatChanged: (String) -> Unit) {
    val (remonteeCategory, pisteIndex) = getRemonteeCategoryAndIndexByName(remontee.name) ?: return
    val remonteeRef = CallDataBase.database.getReference("remontee/${remonteeCategory}/${pisteIndex}/etat")
    remonteeRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val etat = snapshot.getValue(Boolean::class.java) ?: return
            // Convertissez l'état boolean en String ("Ouverte" ou "Fermée") et utilisez onEtatChanged pour mettre à jour l'UI
            onEtatChanged(if (etat) "Ouverte" else "Fermée")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Remontee", "Failed to read value.", error.toException())
        }
    })
}

suspend fun setRemonteeEtat(remontee: Remontee, etat: Boolean) {
    val remonteeCategoryAndIndex = getRemonteeCategoryAndIndexByName(remontee.name)
    if (remonteeCategoryAndIndex != null) {
        val (remonteeCategory, remonteeIndex) = remonteeCategoryAndIndex
        remontee.etat = etat
        val remonteeRef = CallDataBase.database.getReference("remontee").child(remonteeCategory).child(remonteeIndex.toString())
        remonteeRef.setValue(remontee)
    } else {
        Log.e("Remontee", "Piste not found")
    }
}

fun getRemonteeWaiting(remontee: Remontee): String {
    if (remontee.waiting == 0){
        return "0 minutes"
    }
    else if (remontee.waiting == 1){
        return "5 minutes"
    }
    else if (remontee.waiting == 2){
        return "10 minutes"
    }
    else if (remontee.waiting == 3){
        return "15 minutes"
    }
    return "25 minutes et plus"
}

suspend fun getRemonteeWaitingInDatabase(remontee: Remontee, onWaitingChanged: (String) -> Unit) {
    val (remonteeCategory, remonteeIndex) = getRemonteeCategoryAndIndexByName(remontee.name) ?: return
    val remonteeRef = CallDataBase.database.getReference("remontee/${remonteeCategory}/${remonteeIndex}/waiting")
    remonteeRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val waiting = snapshot.getValue(Int::class.java) ?: return
            onWaitingChanged(if (waiting == 0) "5 minutes" else if (waiting == 1) "10 minutes" else if (waiting == 2) "15 minutes" else if (waiting == 3) "20 minutes" else "25 minutes et plus")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Remontee", "Failed to read value.", error.toException())
        }
    })
}

suspend fun setRemonteeWaiting(remontee: Remontee, waiting: Int) {
    val remonteeCategoryAndIndex = getRemonteeCategoryAndIndexByName(remontee.name)
    if (remonteeCategoryAndIndex != null) {
        val (remonteeCategory, remonteeIndex) = remonteeCategoryAndIndex
        remontee.waiting = waiting
        val remonteeRef = CallDataBase.database.getReference("remontee").child(remonteeCategory).child(remonteeIndex.toString())
        remonteeRef.setValue(remontee)
    } else {
        Log.e("Remontee", "Remontee not found")
    }
}

