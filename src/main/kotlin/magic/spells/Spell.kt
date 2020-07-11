package magic.spells

import traveling.position.Distances
import status.stat.FOCUS
import magic.castSpell.CastSpellEvent
import status.conditions.AddConditionEvent
import status.conditions.Condition
import status.statChanged.StatChangeEvent
import core.events.EventManager

open class Spell(
        val name: String,
        val condition: Condition,
        val cost: Int,
        val statRequired: String,
        val levelRequired: Int,
        val range: Int = Distances.BOW_RANGE,
        val castTime: Int = cost,
        val isHostile: Boolean = true
) {
    open fun cast(event: CastSpellEvent) {
        EventManager.postEvent(StatChangeEvent(event.source, "Casting", FOCUS, -event.spell.cost))
        EventManager.postEvent(AddConditionEvent(event.target.target, event.spell.condition))
    }
}