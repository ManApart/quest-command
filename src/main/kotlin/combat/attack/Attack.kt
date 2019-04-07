package combat.attack

import combat.battle.position.HitLevel
import combat.battle.position.TargetDistance
import combat.battle.position.TargetPosition
import combat.takeDamage.TakeDamageEvent
import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.body.Body
import core.gameState.body.BodyPart
import core.gameState.getCreature
import core.gameState.isPlayer
import core.gameState.stat.BARE_HANDED
import core.history.display
import core.history.displayIf
import core.utility.StringFormatter
import core.utility.random
import interact.UseEvent
import system.EventManager

class Attack : EventListener<AttackEvent>() {

    override fun execute(event: AttackEvent) {
        val subject = StringFormatter.getSubject(event.source)

        val defender = event.target.getCreature()
        val defenderName = StringFormatter.getSubject(event.target)

        val offensiveDamage = getOffensiveDamage(event.source, event.sourcePart, event.type)
        val damageSource = event.sourcePart.getEquippedWeapon()?.name ?: event.sourcePart.name

        val range = event.sourcePart.getEquippedWeapon()?.getRange() ?: TargetDistance.DAGGER
        val targetDistance = GameState.battle?.targetDistance ?: range

        if (range < targetDistance) {
            display("${event.target} is too far away to be hit by $damageSource.")
        } else if (defender != null && offensiveDamage > 0) {
            val defenderPosition = GameState.battle?.getCombatant(defender)?.position ?: TargetPosition()
            val adjustedPosition = event.targetPosition.shift(defenderPosition.invert())

            val attackedPart = getAttackedPart(adjustedPosition, defender.body)
            if (attackedPart == null) {
                printDodge(defenderName, defenderPosition)
                display("$subject ${StringFormatter.format(event.source.isPlayer(), "miss", "misses")}!")
            } else {
                val possessive = StringFormatter.getSubjectPossessive(event.source)
                val verb = StringFormatter.format(event.source.isPlayer(), event.type.name.toLowerCase(), event.type.verb)
                val hitLevel = adjustedPosition.getHitLevel(attackedPart.position)
                val hitLevelString = StringFormatter.format(hitLevel == HitLevel.DIRECT, "directly", "grazingly")

                displayIf("$subject $verb towards the ${event.targetPosition}.", !event.targetPosition.equals(TargetPosition()))
                printDodge(defenderName, defenderPosition)
                display("$subject $hitLevelString $verb the ${attackedPart.name} of ${event.target.name} with $possessive $damageSource.")

                EventManager.postEvent(TakeDamageEvent(defender, attackedPart, offensiveDamage, hitLevel, event.type, damageSource))
            }
        } else if (event.sourcePart.getEquippedWeapon() != null) {
            EventManager.postEvent(UseEvent(GameState.player.creature, event.sourcePart.getEquippedWeapon()!!, event.target))
        } else {
            display("Nothing happens.")
        }
        event.target.consume(event)
    }

    private fun printDodge(defenderName: String, defenderPosition: TargetPosition) {
        displayIf("$defenderName dodged to the $defenderPosition.", !defenderPosition.equals(TargetPosition()))
    }

    private fun getAttackedPart(targetPosition: TargetPosition, defender: Body): BodyPart? {
        var parts = defender.getDirectParts(targetPosition)
        if (parts.isEmpty()) {
            parts = defender.getGrazedParts(targetPosition)
        }
        return parts.random()
    }

    private fun getOffensiveDamage(sourceCreature: Creature, sourcePart: BodyPart, type: AttackType): Int {
        return when {
            sourcePart.getEquippedWeapon() != null -> sourcePart.getEquippedWeapon()!!.properties.values.getInt(type.damage, 0)
            else -> sourceCreature.soul.getCurrent(BARE_HANDED)
        }
    }


}