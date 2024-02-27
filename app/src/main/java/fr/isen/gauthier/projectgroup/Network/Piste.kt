package fr.isen.gauthier.projectgroup.Network

data class Piste (
    var affluence : Int = 0,
    var cross: List<Cross> = listOf(),
    var etat : Boolean = true,
    var name: String = " ",
    var visibility : Int = 0
)

class PisteCategory (
    var code: String = "",
    var pistes: List<Piste> = listOf()
)