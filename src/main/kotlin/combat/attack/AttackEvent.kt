package combat.attack

import combat.DamageType
import combat.battle.position.TargetAim
import core.events.Event
import core.target.Target
import traveling.location.location.LocationRecipe


class AttackEvent(val source: Target, val sourcePart: LocationRecipe, val target: TargetAim, val type: DamageType) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}