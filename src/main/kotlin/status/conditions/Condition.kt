package status.conditions

import status.Soul
import core.utility.NameSearchableList
import core.utility.Named
import core.events.EventManager
import status.effects.Effect
import magic.Element
import magic.ElementInteraction

class Condition(
        override val name: String,
        private val element: Element = Element.NONE,
        var elementStrength: Int = 1,
        effects: List<Effect> = listOf(),
        criticalEffects: List<Effect> = listOf(),
        private val permanent: Boolean = false
) : Named {
    private val effects = NameSearchableList(effects)
    private val criticalEffects = NameSearchableList(criticalEffects)

    private var age = 0
    var isCritical = false
    private var isFirstApply = true

    override fun toString(): String {
        return name + ": " + effects.joinToString(", ") { it.base.name }
    }

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

    fun getAge(): Int {
        return age
    }

    fun hasEffect(name: String): Boolean {
        return effects.exists(name) || criticalEffects.exists(name)
    }

}

