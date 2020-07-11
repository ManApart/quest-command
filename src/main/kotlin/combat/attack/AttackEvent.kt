package combat.attack

import combat.DamageType
import traveling.position.TargetAim
import core.events.Event
import core.target.Target
import traveling.location.location.Location


class AttackEvent(val source: Target, val sourcePart: Location, val target: TargetAim, val type: DamageType) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}