package combat.attack

import combat.DamageType
import core.events.Event
import core.thing.Thing
import traveling.location.location.Location
import traveling.position.ThingAim


class AttackEvent(val source: Thing, val sourcePart: Location, val aim: ThingAim, val type: DamageType) : Event {
    override fun gameTicks(): Int = 1
}