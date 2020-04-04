package combat.takeDamage

import combat.DamageType
import core.events.Event
import core.target.Target
import traveling.location.location.Location

class TakeDamageEvent(val source: Target, val sourcePart: Location, val damage: Int, val attackType: DamageType, val damageSource: String) : Event