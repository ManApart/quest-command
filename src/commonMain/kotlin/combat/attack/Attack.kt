package combat.attack

import combat.DamageType
import combat.takeDamage.TakeDamageEvent
import core.body.BodyPart
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.utility.asSubject
import core.utility.asSubjectPossessive
import core.utility.then
import explore.listen.addSoundEffect
import status.stat.AttributeStrings
import status.stat.SkillStrings.BARE_HANDED
import traveling.position.Distances
import traveling.position.ThingAim
import use.UseEvent

class Attack : EventListener<AttackEvent>() {

    override suspend fun complete(event: AttackEvent) {
        val source = event.creature
        val thingDistance = source.position.getDistance(event.aim.thing.position)
        val weaponRange = getRange(source, event.sourcePart)
        val damageSource = event.sourcePart.getEquippedWeapon()?.name ?: event.sourcePart.name
        if (event.aim.thing.soul.getCurrent(AttributeStrings.HEALTH) > 0) {
            val offensiveDamage = getOffensiveDamage(source, event.sourcePart, event.type)

            when {
                weaponRange < thingDistance -> event.creature.displayToMe("${event.aim} is too far away to be hit by $damageSource.")
                offensiveDamage > 0 -> processAttack(event, damageSource, offensiveDamage)
                event.sourcePart.getEquippedWeapon() != null -> EventManager.postEvent(
                    UseEvent(
                        event.creature,
                        event.sourcePart.getEquippedWeapon()!!,
                        event.aim.thing
                    )
                )

                else -> event.creature.displayToMe("Nothing happens.")
            }
        } else {
            when {
                weaponRange < thingDistance -> event.creature.displayToMe("${event.aim} is too far away for $damageSource to be used on it.")
                else -> event.aim.thing.consume(event)
            }
        }
    }

    private suspend fun processAttack(event: AttackEvent, damageSource: String, offensiveDamage: Int) {
        val source = event.creature
        val attackedParts = getAttackedParts(source, event.sourcePart, event.aim)
        if (source != event.aim.thing) {
            source.mind.setAggroTarget(event.aim.thing)
        }

        if (attackedParts.isEmpty()) {
            val missedParts = event.aim.parts.joinToString(", ") { it.name }
            source.display { listener ->
                val subject = source.asSubject(listener)
                "$subject ${source.isPlayer().then("miss", "misses")} $missedParts!"
            }
        } else {
            val verb = event.creature.isPlayer().then(event.type.verbPlural, event.type.verb)
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

    private fun processSound(event: AttackEvent, attackedParts: List<BodyPart>) {
        val soundLevel = if (attackedParts.isEmpty()) 10 else 30
        event.creature.addSoundEffect("Attacking", "the din of battle", soundLevel)
    }

    private fun getAttackedParts(source: Thing, sourcePart: BodyPart, target: ThingAim): List<BodyPart> {
        val sourcePosition = source.position
        val range = getRange(source, sourcePart)
        val distance = sourcePosition.getDistance(target.thing.position)
        return if (range >= distance) target.parts else emptyList()
    }

    private fun getRange(source: Thing, sourcePart: BodyPart): Int {
        val weaponRange = sourcePart.getEquippedWeapon()?.properties?.getRange() ?: Distances.MIN_RANGE
        val bodyRange = source.getRange()
        return weaponRange + bodyRange
    }

    private suspend fun processAttackHit(
        event: AttackEvent,
        attackedPart: BodyPart,
        verb: String,
        damageSource: String,
        defender: ThingAim,
        offensiveDamage: Int
    ) {
        event.creature.display { listener ->
            val subject = event.creature.asSubject(listener)
            val defenderName = event.aim.thing.asSubject(listener)
            val possessive = event.creature.asSubjectPossessive(listener)
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

    private fun getOffensiveDamage(sourceCreature: Thing, sourcePart: BodyPart, type: DamageType): Int {
        return when {
            sourcePart.getEquippedWeapon() != null -> sourcePart.getEquippedWeapon()!!.properties.values.getInt(
                type.damage,
                0
            )

            else -> sourceCreature.soul.getCurrent(BARE_HANDED)
        }
    }


}
