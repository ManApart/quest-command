package interact.magic

import combat.battle.position.TargetDistance
import status.effects.Condition

class Spell(val name: String, val condition: Condition, val cost: Int, val range: TargetDistance = TargetDistance.SPEAR, val castTime: Int = cost, val isHostile: Boolean = true )