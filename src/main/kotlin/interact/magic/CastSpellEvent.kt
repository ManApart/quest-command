package interact.magic

import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.Target
import interact.magic.spells.Spell

class CastSpellEvent(val source: Target, val target: TargetAim, val spell: Spell) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}