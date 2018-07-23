package combat.chop

import core.events.EventListener
import core.gameState.Activator
import core.gameState.BodyPart
import core.gameState.Creature
import core.gameState.Target
import core.gameState.stat.Stat
import status.statChanged.StatChangeEvent
import system.EventManager

class Chop : EventListener<ChopEvent>() {
    val chopHealth = "chop-health"

    override fun execute(event: ChopEvent) {
        println("You chop at ${event.target} with your ${event.sourcePart.equippedName()}.")

        val target = getCreature(event.target)
        val damageDone = getDamageDone(event.sourcePart)

        if (target != null && damageDone > 0) {
            if (hasChopHealth(target)) {
                EventManager.postEvent(StatChangeEvent(target, event.sourcePart.equippedName(), chopHealth, -damageDone))
            } else {
                EventManager.postEvent(StatChangeEvent(target, event.sourcePart.equippedName(), Stat.HEALTH, -damageDone))
            }
        } else {
            println("But nothing happens.")
        }
    }

    private fun getCreature(target: Target): Creature? {
        return when (target) {
            is Creature -> target
            is Activator -> target.creature
            else -> null
        }
    }

    private fun getDamageDone(source: BodyPart): Int {
        return if (source.equippedItem != null){
            source.equippedItem!!.properties.values.getInt("chopDamage", 0)
        } else {
            //TODO - replace with damage based on unarmed skill
            0
        }
    }

    private fun hasChopHealth(target: Creature): Boolean {
        return target.soul.hasStat(chopHealth)
    }


}