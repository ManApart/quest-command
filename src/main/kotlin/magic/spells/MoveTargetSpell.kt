package magic.spells

import core.events.EventManager
import magic.castSpell.CastSpellEvent
import status.conditions.AddConditionEvent
import status.conditions.Condition
import status.stat.FOCUS
import status.statChanged.StatChangeEvent
import traveling.position.Vector
import traveling.move.MoveEvent

class MoveTargetSpell(
        name: String,
        val vector: Vector,
        condition: Condition,
        cost: Int,
        statRequired: String,
        levelRequired: Int,
        range: Int = 1,
        castTime: Int = cost,
        isHostile: Boolean = true
) : Spell(name, condition, cost, statRequired, levelRequired, range, castTime, isHostile) {

    override fun cast(event: CastSpellEvent) {
        EventManager.postEvent(StatChangeEvent(event.source, "Casting", FOCUS, -event.spell.cost))
        EventManager.postEvent(AddConditionEvent(event.target.target, event.spell.condition))
        EventManager.postEvent(MoveEvent(event.target.target, event.target.target.position, vector, 0f))
    }
}