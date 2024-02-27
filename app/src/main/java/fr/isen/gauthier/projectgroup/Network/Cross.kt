package fr.isen.gauthier.projectgroup.Network

import java.io.Serializable

data class Cross (
    var end: List<String> = listOf(),
    var start: List<String> = listOf(),
    var middle: List<String> = listOf()
): Serializable