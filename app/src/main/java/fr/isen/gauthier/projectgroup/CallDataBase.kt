package fr.isen.gauthier.projectgroup

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CallDataBase {
    companion object {
        val database = Firebase.database("https://projectgroup-4baa2-default-rtdb.europe-west1.firebasedatabase.app/")
    }
}