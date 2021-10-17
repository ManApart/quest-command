package combat.attack

import combat.DamageType
import combat.takeDamage.TakeDamageEvent
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.thing.Thing
import core.utility.asSubject
import core.utility.asSubjectPossessive
import core.utility.then
import status.stat.BARE_HANDED
import status.stat.HEALTH
import traveling.location.location.Location
import traveling.position.Distances
import traveling.position.ThingAim
import use.UseEvent

class Attack : EventListener<AttackEvent>() {

    override fun execute(event: AttackEvent) {
        val source = event.source
        if (event.thing.thing.soul.getCurrent(HEALTH) > 0) {
            val offensiveDamage = getOffensiveDamage(source, event.sourcePart, event.type)
            val damageSource = event.sourcePart.getEquippedWeapon()?.name ?: event.sourcePart.name
            val thingDistance = source.position.getDistance(event.thing.thing.position)
            val weaponRange = getRange(source, event.sourcePart)

            when {
                weaponRange < thingDistance -> event.source.display("${event.thing} is too far away to be hit by $damageSource.")
                offensiveDamage > 0 -> processAttack(event, damageSource, offensiveDamage)
                event.sourcePart.getEquippedWeapon() != null -> EventManager.postEvent(
                    UseEvent(
                        event.source,
                        event.sourcePart.getEquippedWeapon()!!,
                        event.thing.thing
                    )
                )
                else -> event.source.display("Nothing happens.")
            }
        }
        event.thing.thing.consume(event)
    }

    private fun processAttack(event: AttackEvent, damageSource: String, offensiveDamage: Int) {
        val source = event.source
        val attackedParts = getAttackedParts(source, event.sourcePart, event.thing)
        if (source != event.thing.thing) {
            source.ai.aggroThing = event.thing.thing
        }

        if (attackedParts.isEmpty()) {
            val missedParts = event.thing.bodyPartThings.joinToString(", ") { it.name }
            source.display { listener ->
                val subject = source.asSubject(listener)
                "$subject ${source.isPlayer().then("miss", "misses")} $missedParts!"
            }
        } else {
            val verb = event.source.isPlayer().then(event.type.verbPlural, event.type.verb)
//            display("$subject $verb at $defenderName.")
            attackedParts.forEach { attackedPart ->
                processAttackHit(
                    event,
                    attackedPart,
                    verb,
                    damageSource,
                    event.thing,
                    offensiveDamage
                )
            }
        }
    }

    private fun getAttackedParts(source: Thing, sourcePart: Location, thing: ThingAim): List<Location> {
        val sourcePosition = source.getPositionInLocation(sourcePart)
        val range = getRange(source, sourcePart)
        return thing.bodyPartThings.filter {
            val thingPartPosition = thing.thing.getPositionInLocation(it)
            val distance = sourcePosition.getDistance(thingPartPosition)
            range >= distance
        }
    }

    private fun getRange(source: Thing, sourcePart: Location): Int {
        val weaponRange = sourcePart.getEquippedWeapon()?.properties?.getRange() ?: Distances.MIN_RANGE
        val bodyRange = source.body.getRange()
        return weaponRange + bodyRange
    }

    private fun processAttackHit(
        event: AttackEvent,
        attackedPart: Location,
        verb: String,
        damageSource: String,
        defender: ThingAim,
        offensiveDamage: Int
    ) {
        event.source.display { listener ->
            val subject = event.source.asSubject(listener)
            val defenderName = event.thing.thing.asSubject(listener)
            val possessive = event.source.asSubjectPossessive(listener)
            "$subject $verb the ${attackedPart.name} of $defenderName with $possessive $damageSource."
        }
        EventManager.postEvent(
            TakeDamageEvent(
                defender.thing,
                attackedPart,
                offensiveDamage,
                event.type,
                damageSource
            )
        )
    }

    private fun getOffensiveDamage(sourceCreature: Thing, sourcePart: Location, type: DamageType): Int {
        return when {
            sourcePart.getEquippedWeapon() != null -> sourcePart.getEquippedWeapon()!!.properties.values.getInt(
                type.damage,
                0
            )
            else -> sourceCreature.soul.getCurrent(BARE_HANDED)
        }
    }


}