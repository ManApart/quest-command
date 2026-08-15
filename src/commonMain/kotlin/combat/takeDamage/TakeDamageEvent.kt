package combat.takeDamage

import combat.DamageType
import core.body.BodyPart
import core.events.Event
import core.thing.Thing

data class TakeDamageEvent(val source: Thing, val sourcePart: BodyPart, val damage: Int, val attackType: DamageType, val damageSource: String) : Event
