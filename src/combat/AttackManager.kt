package combat

import combat.chop.ChopEvent
import combat.slash.SlashEvent
import combat.stab.StabEvent
import core.events.Event
import core.events.EventListener
import core.gameState.*
import core.gameState.Target
import core.gameState.stat.Stat
import status.statChanged.StatChangeEvent
import system.EventManager

object AttackManager {
    enum class AttackType(val health: String, val damage: String) {
        CHOP("chop-health", "chopDamage"),
        SLASH("slashHealth", "slashDamage"),
        STAB("stabHealth", "stabDamage");
    }

    class Chop : EventListener<ChopEvent>() {
        override fun execute(event: ChopEvent) {
            AttackManager.execute(AttackType.CHOP, event.source, event.sourcePart, event.target, event)
        }
    }

    class Stab : EventListener<StabEvent>() {
        override fun execute(event: StabEvent) {
            AttackManager.execute(AttackType.STAB, event.source, event.sourcePart, event.target, event)
        }
    }

    class Slash : EventListener<SlashEvent>() {
        override fun execute(event: SlashEvent) {
            AttackManager.execute(AttackType.SLASH, event.source, event.sourcePart, event.target, event)
        }
    }

    fun execute(type: AttackType, source: Creature, sourcePart: BodyPart, target: Target, event: Event) {
        println("You ${type.name.toLowerCase()} at ${target.name} with your ${sourcePart.equippedName()}.")

        val creature = getCreature(target)
        val damageDone = getDamageDone(sourcePart, type)

        if (creature != null && damageDone > 0) {
            if (hasSpecificHealth(creature, type)) {
                EventManager.postEvent(StatChangeEvent(creature, sourcePart.equippedName(), type.health, -damageDone))
            } else if (creature.soul.hasStat(Stat.HEALTH)) {
                EventManager.postEvent(StatChangeEvent(creature, sourcePart.equippedName(), Stat.HEALTH, -damageDone))
            }
        } else {
            println("Nothing happens.")
        }
        target.consume(event)
    }

    private fun getDamageDone(source: BodyPart, type: AttackType): Int {
        return if (source.equippedItem != null){
            source.equippedItem!!.properties.values.getInt(type.damage, 0)
        } else {
            //TODO - replace with damage based on unarmed skill
            0
        }
    }

    private fun hasSpecificHealth(target: Creature, attackType: AttackType): Boolean {
        return target.soul.hasStat(attackType.health)
    }

}