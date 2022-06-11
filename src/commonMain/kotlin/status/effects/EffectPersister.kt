package status.effects

import core.body.Body

@kotlinx.serialization.Serializable
data class EffectP(
    val base: EffectBase,
    val amount: Int,
    val duration: Int,
    val bodyPartTargets: List<String>
    ){
    constructor(b: Effect): this(b.base, b.amount, b.duration, b.bodyPartTargets.map { it.name })

    fun parsed(body: Body): Effect {
        return Effect(base, amount, duration, bodyPartTargets.map { body.getPart(it) })
    }
}