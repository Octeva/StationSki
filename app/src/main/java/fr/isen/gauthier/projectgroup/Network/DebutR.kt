package fr.isen.gauthier.projectgroup.Network

import java.io.Serializable

data class DebutR (
    val endP : List<String> = listOf(),
    val startP : List<String> = listOf(),
    val crossP : List<String> = listOf()
) : Serializable