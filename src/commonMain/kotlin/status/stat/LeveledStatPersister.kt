package status.stat

@kotlinx.serialization.Serializable
data class LeveledStatP(
    val name: String,
    val level: Int,
    val maxMultiplier: Int,
    val expExponential: Int,
    val max: Int,
    val current: Int,
    val xp: Double,

){
    constructor(b: LeveledStat): this(b.name, b.level, b.getMaxMultiplier(), b.expExponential, b.max, b.current, b.xp)

    fun parsed(): LeveledStat {
        return LeveledStat(name, level, maxMultiplier, expExponential, max, current, xp)
    }
}