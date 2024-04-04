package fr.isen.gauthier.projectgroup.Network

data class StartRemontee (
    val endP : List<String> = listOf(),
    val startP : List<String> = listOf(),
    val crossP: List<String> = listOf()
)