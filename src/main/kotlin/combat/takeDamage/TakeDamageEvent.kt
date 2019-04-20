package combat.takeDamage

import combat.attack.AttackType
import combat.battle.position.HitLevel
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart

class TakeDamageEvent(val source: Target, val sourcePart: BodyPart, val damage: Int, val hitLevel: HitLevel, val attackType: AttackType, val damageSource: String) : Event