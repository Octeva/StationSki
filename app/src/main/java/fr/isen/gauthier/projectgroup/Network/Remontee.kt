package fr.isen.gauthier.projectgroup.Network

import java.io.Serializable

data class Remontee (
    val difficulty : Int = 0,
    val endRemontee : List<Remontee> = listOf(),
    val etat : Boolean = true,
    val name : String = "",
    val startRemontee : List<DebutR> = listOf(),
    val  waiting : Int = 0
) : Serializable

class RemonteCategory (
    var code: String = "",
    var remontee: List<Remontee> = listOf()
)