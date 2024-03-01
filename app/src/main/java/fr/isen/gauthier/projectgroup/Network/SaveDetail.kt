package fr.isen.gauthier.projectgroup.Network

class SaveDetail {
    private var savedPiste: Piste? = null

    fun save(piste: Piste) {
        savedPiste = piste
    }

    fun getPiste(): Piste {
        return savedPiste ?: Piste() // Retourne une nouvelle instance de Piste si aucune n'est sauvegardée
    }
}