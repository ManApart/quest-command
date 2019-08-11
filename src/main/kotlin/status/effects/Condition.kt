package status.effects

import core.gameState.Soul
import core.utility.Named
import system.EventManager

class Condition(
        override val name: String,
        private val element: Element = Element.NONE,
        var elementStrength: Int = 0,
        private var duration: Int = 1,
        private val permenant: Boolean = false,
        private val effects: List<Effect>,
        private val criticalEffects: List<Effect> = listOf()
): Named {
    var isCritical = false
    private var isFirstApply = true

    fun getReaction(other: Condition): ElementInteraction {
        return element.getReaction(elementStrength, other.element, other.elementStrength)
    }

    private fun getEffects(): List<Effect> {
        return if (isCritical) {
            effects + criticalEffects
        } else {
            effects
        }
    }

    fun apply(soul: Soul) {
        getEffects().forEach { it.apply(soul, isFirstApply) }
        decreaseDuration(soul)
        isFirstApply = false
    }

    private fun decreaseDuration(soul: Soul) {
        if (!permenant) {
            if (duration > 0) duration--
            if (duration == 0) {
                EventManager.postEvent(RemoveConditionEvent(soul.parent, this))
            }
        }
    }

    fun removeEffects(soul: Soul) {
        getEffects().forEach { it.remove(soul) }
    }

}

