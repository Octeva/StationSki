package fr.isen.gauthier.projectgroup.Network

data class Pistes (
    val affluence : Int,
    val cross: List<Cross>,
    val etat : Boolean,
    val name: String,
    val visibility : Int
)
