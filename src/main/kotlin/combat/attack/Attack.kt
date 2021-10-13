package combat.attack

import combat.DamageType
import combat.takeDamage.TakeDamageEvent
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.asSubject
import core.utility.asSubjectPossessive
import core.utility.then
import status.stat.BARE_HANDED
import status.stat.HEALTH
import traveling.location.location.Location
import traveling.position.Distances
import traveling.position.TargetAim
import use.UseEvent

class Attack : EventListener<AttackEvent>() {

    override fun execute(event: AttackEvent) {
        val source = event.source
        if (event.target.target.soul.getCurrent(HEALTH) > 0) {
            val offensiveDamage = getOffensiveDamage(source, event.sourcePart, event.type)
            val damageSource = event.sourcePart.getEquippedWeapon()?.name ?: event.sourcePart.name
            val targetDistance = source.position.getDistance(event.target.target.position)
            val weaponRange = getRange(source, event.sourcePart)

            when {
                weaponRange < targetDistance -> event.source.display("${event.target} is too far away to be hit by $damageSource.")
                offensiveDamage > 0 -> processAttack(event, damageSource, offensiveDamage)
                event.sourcePart.getEquippedWeapon() != null -> EventManager.postEvent(
                    UseEvent(
                        event.source,
                        event.sourcePart.getEquippedWeapon()!!,
                        event.target.target
                    )
                )
                else -> event.source.display("Nothing happens.")
            }
        }
        event.target.target.consume(event)
    }

    private fun processAttack(event: AttackEvent, damageSource: String, offensiveDamage: Int) {
        val source = event.source
        val attackedParts = getAttackedParts(source, event.sourcePart, event.target)
        if (source != event.target.target) {
            source.ai.aggroTarget = event.target.target
        }

        if (attackedParts.isEmpty()) {
            val missedParts = event.target.bodyPartTargets.joinToString(", ") { it.name }
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
                    event.target,
                    offensiveDamage
                )
            }
        }
    }

    private fun getAttackedParts(source: Target, sourcePart: Location, target: TargetAim): List<Location> {
        val sourcePosition = source.getPositionInLocation(sourcePart)
        val range = getRange(source, sourcePart)
        return target.bodyPartTargets.filter {
            val targetPartPosition = target.target.getPositionInLocation(it)
            val distance = sourcePosition.getDistance(targetPartPosition)
            range >= distance
        }
    }

    private fun getRange(source: Target, sourcePart: Location): Int {
        val weaponRange = sourcePart.getEquippedWeapon()?.properties?.getRange() ?: Distances.MIN_RANGE
        val bodyRange = source.body.getRange()
        return weaponRange + bodyRange
    }

    private fun processAttackHit(
        event: AttackEvent,
        attackedPart: Location,
        verb: String,
        damageSource: String,
        defender: TargetAim,
        offensiveDamage: Int
    ) {
        event.source.display { listener ->
            val subject = event.source.asSubject(listener)
            val defenderName = event.target.target.asSubject(listener)
            val possessive = event.source.asSubjectPossessive(listener)
            "$subject $verb the ${attackedPart.name} of $defenderName with $possessive $damageSource."
        }
        EventManager.postEvent(
            TakeDamageEvent(
                defender.target,
                attackedPart,
                offensiveDamage,
                event.type,
                damageSource
            )
        )
    }

    private fun getOffensiveDamage(sourceCreature: Target, sourcePart: Location, type: DamageType): Int {
        return when {
            sourcePart.getEquippedWeapon() != null -> sourcePart.getEquippedWeapon()!!.properties.values.getInt(
                type.damage,
                0
            )
            else -> sourceCreature.soul.getCurrent(BARE_HANDED)
        }
    }


}