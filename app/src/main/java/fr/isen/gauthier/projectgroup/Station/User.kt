package fr.isen.gauthier.projectgroup.Station

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.isen.gauthier.projectgroup.CallDataBase
import fr.isen.gauthier.projectgroup.Network.Remontee
import java.io.Serializable
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class User(
    var id:Int = 0,
    var email:String ="",
    var pseudo:String=""
): Serializable

class UserCategory (
    var codeR: String = "",
    var users: List<User> = listOf()
)

fun getUserPseudo(pseudo: String, onUserRetrieved: (User) -> Unit) {
    val userRef = CallDataBase.database.getReference("users")
    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
            val user = users.find { it.pseudo == pseudo }
            if (user != null) {
                onUserRetrieved(user)
            } else {
                Log.e("User", "User not found")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("User", "Failed to read value.", error.toException())
        }
    })
}

fun setUserPseudo(pseudo: String, user: User) {
    val userRef = CallDataBase.database.getReference("users")
    userRef.child(pseudo).setValue(user)
}