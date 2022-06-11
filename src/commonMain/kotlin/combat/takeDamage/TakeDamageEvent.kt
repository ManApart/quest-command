package combat.takeDamage

import combat.DamageType
import core.events.Event
import core.thing.Thing
import traveling.location.location.Location

class TakeDamageEvent(val source: Thing, val sourcePart: Location, val damage: Int, val attackType: DamageType, val damageSource: String) : Event