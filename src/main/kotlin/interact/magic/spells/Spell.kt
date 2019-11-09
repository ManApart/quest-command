package interact.magic.spells

import core.gameState.stat.FOCUS
import interact.magic.CastSpellEvent
import status.effects.AddConditionEvent
import status.effects.Condition
import status.statChanged.StatChangeEvent
import system.EventManager

open class Spell(
        val name: String,
        val condition: Condition,
        val cost: Int,
        val statRequired: String,
        val levelRequired: Int,
        val range: Int = 1,
        val castTime: Int = cost,
        val isHostile: Boolean = true
) {
    open fun cast(event: CastSpellEvent) {
        EventManager.postEvent(StatChangeEvent(event.source, "Casting", FOCUS, -event.spell.cost))
        EventManager.postEvent(AddConditionEvent(event.target.target, event.spell.condition))
    }
}