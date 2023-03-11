package magic.spells

import core.events.EventManager
import magic.castSpell.CastSpellEvent
import status.conditions.AddConditionEvent
import status.conditions.Condition
import status.stat.FOCUS
import status.statChanged.StatChangeEvent
import traveling.position.Distances

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
        EventManager.postEvent(StatChangeEvent(event.creature, "Casting", FOCUS, -event.spell.cost))
        EventManager.postEvent(AddConditionEvent(event.thing.thing, event.spell.condition))
    }
}