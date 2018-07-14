package core.gameState

class Stat(val type: String, var max: Int) {
    companion object {
        //Attributes
        const val AGILITY = "Agility"
        const val HEALTH = "Health"
        const val STAMINA = "Stamina"
        const val STRENGTH = "Strength"

        //Skills
        const val CLIMBING = "Climbing"
        const val BARE_FOOT = "Barefoot"
    }

    var current: Int = max


}
