package interact.magic.spells

import combat.approach.MoveEvent
import core.gameState.Vector
import core.gameState.stat.FOCUS
import interact.magic.CastSpellEvent
import status.effects.AddConditionEvent
import status.effects.Condition
import status.statChanged.StatChangeEvent
import system.EventManager

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
        EventManager.postEvent(MoveEvent(event.target.target, vector, false))
    }
}