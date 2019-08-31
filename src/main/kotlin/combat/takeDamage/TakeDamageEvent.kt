package combat.takeDamage

import combat.DamageType
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart

class TakeDamageEvent(val source: Target, val sourcePart: BodyPart, val damage: Int, val attackType: DamageType, val damageSource: String) : Event