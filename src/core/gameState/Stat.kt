package core.gameState

class Stat(val type: String, val max: Int) {
    companion object {
        const val HEALTH = "Health"
        const val STAMINA = "Stamina"
    }
    var current: Int = max


}
