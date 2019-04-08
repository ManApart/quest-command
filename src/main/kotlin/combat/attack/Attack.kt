package combat.attack

import combat.Combatant
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

        if (isValidAttack(event)) {
            val defender = GameState.battle!!.getCombatant(event.target.getCreature()!!)!!
            val offensiveDamage = getOffensiveDamage(event.source, event.sourcePart, event.type)
            val damageSource = event.sourcePart.getEquippedWeapon()?.name ?: event.sourcePart.name
            val weaponRange = event.sourcePart.getEquippedWeapon()?.getRange() ?: TargetDistance.DAGGER
            val targetDistance = GameState.battle?.targetDistance ?: weaponRange

            when {
                weaponRange < targetDistance -> display("${event.target} is too far away to be hit by $damageSource.")
                offensiveDamage > 0 -> processAttack(defender, event, damageSource, offensiveDamage)
                event.sourcePart.getEquippedWeapon() != null -> EventManager.postEvent(UseEvent(GameState.player.creature, event.sourcePart.getEquippedWeapon()!!, event.target))
                else -> display("Nothing happens.")
            }
        }
        event.target.consume(event)
    }

    private fun isValidAttack(event: AttackEvent): Boolean {
        return when {
            event.target.getCreature() == null -> {
                println("Attack had no valid target.")
                false
            }
            GameState.battle == null -> {
                println("Attack has no battle context.")
                false
            }
            GameState.battle?.getCombatant(event.target.getCreature()!!) == null -> {
                println("Attack battle has no combatant for target.")
                false
            }
            else -> true
        }
    }

    private fun processAttack(defender: Combatant, event: AttackEvent, damageSource: String, offensiveDamage: Int) {
        val subject = StringFormatter.getSubject(event.source)
        val defenderName = StringFormatter.getSubject(event.target)
        val attackedPart = AttackedPartHelper(defender, event.targetPosition).getAttackedPart()

        if (attackedPart == null) {
            printDodge(defenderName, defender.position)
            display("$subject ${StringFormatter.format(event.source.isPlayer(), "miss", "misses")}!")
        } else {
            processAttackHit(event, attackedPart, subject, defenderName, defender.position, damageSource, defender, offensiveDamage)
        }
    }

    private fun processAttackHit(event: AttackEvent, attackedPart: BodyPart, subject: String, defenderName: String, defenderPosition: TargetPosition, damageSource: String, defender: Combatant, offensiveDamage: Int) {
        val possessive = StringFormatter.getSubjectPossessive(event.source)
        val verb = StringFormatter.format(event.source.isPlayer(), event.type.name.toLowerCase(), event.type.verb)
        val adjustedPosition = event.targetPosition.shift(defender.position.invert())
        val hitLevel = adjustedPosition.getHitLevel(attackedPart.position)
        val hitLevelString = StringFormatter.format(hitLevel == HitLevel.DIRECT, "directly", "grazingly")

        displayIf("$subject $verb towards the ${event.targetPosition}.", !event.targetPosition.equals(TargetPosition()))
        printDodge(defenderName, defenderPosition)
        display("$subject $hitLevelString $verb the ${attackedPart.name} of ${event.target.name} with $possessive $damageSource.")

        EventManager.postEvent(TakeDamageEvent(defender.creature, attackedPart, offensiveDamage, hitLevel, event.type, damageSource))
    }

    private fun printDodge(defenderName: String, defenderPosition: TargetPosition) {
        displayIf("$defenderName dodged to the $defenderPosition.", !defenderPosition.equals(TargetPosition()))
    }

    private fun getOffensiveDamage(sourceCreature: Creature, sourcePart: BodyPart, type: AttackType): Int {
        return when {
            sourcePart.getEquippedWeapon() != null -> sourcePart.getEquippedWeapon()!!.properties.values.getInt(type.damage, 0)
            else -> sourceCreature.soul.getCurrent(BARE_HANDED)
        }
    }


}