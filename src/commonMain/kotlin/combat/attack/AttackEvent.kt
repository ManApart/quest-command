package combat.attack

import combat.DamageType
import core.events.TemporalEvent
import core.thing.Thing
import status.stat.AGILITY
import traveling.location.location.Location
import traveling.position.ThingAim
import kotlin.math.max


data class AttackEvent(override val creature: Thing, val sourcePart: Location, val aim: ThingAim, val type: DamageType, override var timeLeft: Int = 1) : TemporalEvent

suspend fun startAttack(source: Thing, sourcePart: Location, thing: ThingAim, type: DamageType, timeLeft: Int? = null): AttackEvent {
    return AttackEvent(source, sourcePart, thing, type, timeLeft ?: calcTimeLeft(source, sourcePart))
}
private suspend fun calcTimeLeft(source: Thing, sourcePart: Location): Int {
    val weaponSize = sourcePart.getEquippedWeapon()?.properties?.getRange() ?: 1
    val weaponWeight = sourcePart.getEquippedWeapon()?.getWeight() ?: 1
    val encumbrance = source.getEncumbrance()
    val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())
    val weaponScaleFactor = 10

    return max(1, weaponSize * weaponWeight * weaponScaleFactor / agility)
}