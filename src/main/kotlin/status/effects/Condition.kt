package status.effects

import core.gameState.Soul
import core.utility.Named
import system.EventManager

class Condition(
        override val name: String,
        private val element: Element = Element.NONE,
        var elementStrength: Int = 1,
        private val effects: List<Effect> = listOf(),
        private val criticalEffects: List<Effect> = listOf(),
        private val permanent: Boolean = false
) : Named {
    private var age = 0
    var isCritical = false
    private var isFirstApply = true

    fun getReaction(other: Condition): ElementInteraction {
        return element.getReaction(elementStrength, other.element, other.elementStrength)
    }

    fun getEffects(): List<Effect> {
        return if (isCritical && criticalEffects.isNotEmpty()) {
            criticalEffects
        } else {
            effects
        }
    }

    fun apply(soul: Soul) {
        var effectApplied = false
        getEffects().forEach { effect ->
            if (permanent || age < effect.duration) {
                effect.apply(soul, isFirstApply)
                effectApplied = true
            }
        }

        if (!effectApplied) {
            EventManager.postEvent(RemoveConditionEvent(soul.parent, this))
        }

        isFirstApply = false
        age++
    }

    fun removeEffects(soul: Soul) {
        getEffects().forEach { it.remove(soul) }
    }

}

