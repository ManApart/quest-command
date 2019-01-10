package combat.battle.position

enum class HitLevel(val modifier: Float) {
    DIRECT(1f), GRAZING(.5f), MISS(0f)
}