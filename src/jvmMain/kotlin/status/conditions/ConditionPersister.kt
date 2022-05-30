package status.conditions

import core.body.Body
import magic.Element
import status.effects.EffectP

@kotlinx.serialization.Serializable
data class ConditionP(
    val name: String,
    val element: Element,
    val elementStrength: Int,
    val effects: List<EffectP>,
    val criticalEffects: List<EffectP>,
    val permanent: Boolean,
    val age: Int,
    val isCritical: Boolean,
    val isFirstApply: Boolean
){
    constructor(b: Condition): this(b.name, b.element, b.elementStrength, b.effects.map { EffectP(it) }, b.criticalEffects.map { EffectP(it) }, b.permanent, b.age, b.isCritical, b.isFirstApply)

    fun parsed(body: Body): Condition {
        return Condition(name, element, elementStrength, effects.map { it.parsed(body) }, criticalEffects.map { it.parsed(body) }, permanent, age, isCritical)
    }
}