package combat.attack

import combat.DamageType
import combat.takeDamage.TakeDamageEvent
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.utility.asSubject
import core.utility.asSubjectPossessive
import core.utility.then
import magic.Element
import status.conditions.Condition
import status.effects.EffectManager
import status.stat.BARE_HANDED
import status.stat.HEALTH
import traveling.location.location.Location
import traveling.position.Distances
import traveling.position.ThingAim
import use.UseEvent

class Attack : EventListener<AttackEvent>() {

    override fun execute(event: AttackEvent) {
        val source = event.source
        val thingDistance = source.position.getDistance(event.aim.thing.position)
        val weaponRange = getRange(source, event.sourcePart)
        val damageSource = event.sourcePart.getEquippedWeapon()?.name ?: event.sourcePart.name
        if (event.aim.thing.soul.getCurrent(HEALTH) > 0) {
            val offensiveDamage = getOffensiveDamage(source, event.sourcePart, event.type)

            when {
                weaponRange < thingDistance -> event.source.displayToMe("${event.aim} is too far away to be hit by $damageSource.")
                offensiveDamage > 0 -> processAttack(event, damageSource, offensiveDamage)
                event.sourcePart.getEquippedWeapon() != null -> EventManager.postEvent(
                    UseEvent(
                        event.source,
                        event.sourcePart.getEquippedWeapon()!!,
                        event.aim.thing
                    )
                )
                else -> event.source.displayToMe("Nothing happens.")
            }
        } else {
            when {
                weaponRange < thingDistance -> event.source.displayToMe("${event.aim} is too far away for $damageSource to be used on it.")
                else -> event.aim.thing.consume(event)
            }
        }
    }

    private fun processAttack(event: AttackEvent, damageSource: String, offensiveDamage: Int) {
        val source = event.source
        val attackedParts = getAttackedParts(source, event.sourcePart, event.aim)
        if (source != event.aim.thing) {
            source.ai.aggroThing = event.aim.thing
        }

        if (attackedParts.isEmpty()) {
            val missedParts = event.aim.bodyPartThings.joinToString(", ") { it.name }
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
                    event.aim,
                    offensiveDamage
                )
            }
        }
        processSound(event, attackedParts)
    }

    private fun processSound(event: AttackEvent, attackedParts: List<Location>) {
        val soundLevel = if (attackedParts.isEmpty()) 10 else 30

        val condition = Condition("Attacking", Element.AIR, 20, listOf(EffectManager.getEffect("Attacking", soundLevel, 2)), silent = true)
        event.source.soul.addNewCondition(condition)
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
            val defenderName = event.aim.thing.asSubject(listener)
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