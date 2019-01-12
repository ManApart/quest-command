package combat.takeDamage

import combat.AttackType
import combat.battle.position.HitLevel
import core.events.Event
import core.gameState.Creature
import core.gameState.body.BodyPart

class TakeDamageEvent(val source: Creature, val sourcePart: BodyPart, val damage: Int, val hitLevel: HitLevel, val attackType: AttackType, val damageSource: String) : Event