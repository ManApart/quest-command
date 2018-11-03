package combat

import combat.chop.ChopEvent
import combat.crush.CrushEvent
import combat.slash.SlashEvent
import combat.stab.StabEvent
import core.events.Event
import core.events.EventListener
import core.gameState.BodyPart
import core.gameState.Creature
import core.gameState.Target
import core.gameState.getCreature
import core.gameState.stat.Stat
import core.utility.StringFormatter
import status.statChanged.StatChangeEvent
import system.EventManager

object AttackManager {
    enum class AttackType(val health: String, val damage: String) {
        CHOP("chop-health", "chopDamage"),
        CRUSH("crushHealth", "crushDamage"),
        SLASH("slashHealth", "slashDamage"),
        STAB("stabHealth", "stabDamage");
    }

    class Chop : EventListener<ChopEvent>() {
        override fun execute(event: ChopEvent) {
            AttackManager.execute(AttackType.CHOP, event.source, event.sourcePart, event.target, event.direction, event)
        }
    }

    class Crush : EventListener<CrushEvent>() {
        override fun execute(event: CrushEvent) {
            AttackManager.execute(AttackType.CRUSH, event.source, event.sourcePart, event.target, event.direction, event)
        }
    }

    class Stab : EventListener<StabEvent>() {
        override fun execute(event: StabEvent) {
            AttackManager.execute(AttackType.STAB, event.source, event.sourcePart, event.target, event.direction, event)
        }
    }

    class Slash : EventListener<SlashEvent>() {
        override fun execute(event: SlashEvent) {
            AttackManager.execute(AttackType.SLASH, event.source, event.sourcePart, event.target, event.direction, event)
        }
    }

    fun execute(type: AttackType, source: Creature, sourcePart: BodyPart, target: Target, direction: TargetDirection, event: Event) {
        val subject = StringFormatter.getSubject(source)
        val possessive = StringFormatter.getSubjectPossessive(source)
        println("$subject ${type.name.toLowerCase()} the $direction of ${target.name} with $possessive ${sourcePart.equippedName()}.")

        val creature = getCreature(target)
        val damageDone = getDamageDone(source, sourcePart, type)

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

    private fun getDamageDone(creature: Creature, source: BodyPart, type: AttackType): Int {
        return when {
            source.equippedItem != null -> source.equippedItem!!.properties.values.getInt(type.damage, 0)
            else -> creature.soul.getCurrent(Stat.BARE_HANDED)
        }
    }

    private fun hasSpecificHealth(target: Creature, attackType: AttackType): Boolean {
        return target.soul.hasStat(attackType.health)
    }

}