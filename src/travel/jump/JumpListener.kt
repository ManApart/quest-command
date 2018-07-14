package travel.jump

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Stat
import status.StatChangeEvent
import system.EventManager
import travel.ArriveEvent

class JumpListener : EventListener<JumpEvent>() {
    override fun shouldExecute(event: JumpEvent): Boolean {
        return event.creature == GameState.player
    }
    override fun execute(event: JumpEvent) {
        println("You jump down.")
        val damage = calculateJumpDamage(event)

        EventManager.postEvent(StatChangeEvent(GameState.player, "Falling", Stat.HEALTH, damage))
        EventManager.postEvent(ArriveEvent(destination = event.destination))
    }

    private fun calculateJumpDamage(event: JumpEvent): Int {
        val soul = event.creature.soul
        val height = Math.abs(event.source.position.z - event.destination.position.z)
        val damage = height - soul.getCurrent(Stat.AGILITY)

        //TODO - look at foot defense and factor that in
        //TODO - factor in encumberance. The higher the encumberance, the greater the damage
        return -Math.max(damage, 0)
    }
}