package status.conditions

import core.events.EventManager
import core.utility.NameSearchableList
import core.utility.Named
import magic.Element
import magic.ElementInteraction
import status.Soul
import status.effects.Effect

class Condition(
        override val name: String,
        val element: Element = Element.NONE,
        var elementStrength: Int = 1,
        effects: List<Effect> = listOf(),
        criticalEffects: List<Effect> = listOf(),
        val permanent: Boolean = false,
        age: Int? = null,
        isCritical: Boolean? = null,
        isFirstApply: Boolean? = null

) : Named {
    val effects = NameSearchableList(effects)
    val criticalEffects = NameSearchableList(criticalEffects)

    var age = age ?: 0; private set
    var isCritical = isCritical ?: false
    var isFirstApply = isFirstApply ?: true; private set

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
        getEffects().forEach { effect ->
            if (permanent || age < effect.duration) {
                effect.apply(soul, isFirstApply)
            }
        }

        isFirstApply = false
        age++
    }

    fun removeEffects(soul: Soul) {
        getEffects().forEach { it.remove(soul) }
    }

    fun hasEffect(name: String): Boolean {
        return effects.exists(name) || criticalEffects.exists(name)
    }

    fun isStillViable(): Boolean {
        return getEffects().any { permanent || age < it.duration }
    }

}

