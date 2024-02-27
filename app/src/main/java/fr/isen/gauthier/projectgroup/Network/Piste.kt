package fr.isen.gauthier.projectgroup.Network

data class Pistes (
    var affluence : Int,
    var cross: List<Cross>,
    var etat : Boolean,
    var name: String,
    var visibility : Int
)
