package combat.battle.position
//TODO - can this go away?
enum class HitLevel(val modifier: Float) {
    DIRECT(1f), GRAZING(.5f), MISS(0f)
}