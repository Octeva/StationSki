package fr.isen.gauthier.projectgroup.Network

data class Remontee (
    val difficulty : Int,
    val endRemontee : List<Remontee>,
    val etat : Boolean,
    val name : String,
    val startRemontee : List<DebutR>,
    val  waiting : Int
)