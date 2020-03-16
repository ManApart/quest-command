package combat.takeDamage

import combat.DamageType
import core.events.Event
import core.target.Target
import traveling.location.location.LocationRecipe

class TakeDamageEvent(val source: Target, val sourcePart: LocationRecipe, val damage: Int, val attackType: DamageType, val damageSource: String) : Event