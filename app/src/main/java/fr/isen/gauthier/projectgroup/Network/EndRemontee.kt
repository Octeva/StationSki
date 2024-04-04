package fr.isen.gauthier.projectgroup.Network

import java.io.Serializable


data class EndRemontee (

val endPiste : List<String> = listOf(),
val startPiste : List<String> = listOf(),
val crossPiste : List<String> = listOf()

): Serializable