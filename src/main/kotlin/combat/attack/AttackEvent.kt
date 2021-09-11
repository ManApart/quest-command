package combat.attack

import combat.DamageType
import core.events.Event
import core.target.Target
import traveling.location.location.Location
import traveling.position.TargetAim


class AttackEvent(val source: Target, val sourcePart: Location, val target: TargetAim, val type: DamageType) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}