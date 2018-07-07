package core.gameState

class Stat(val type: StatType, val max: Int) {
    enum class StatType {HEALTH, STAMINA}
    var current: Int = max


}