package travel.jump

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.Stat
import status.StatChangeEvent
import system.EventManager
import travel.ArriveEvent

class JumpListener : EventListener<JumpEvent>() {
    override fun shouldExecute(event: JumpEvent): Boolean {
        return event.creature == GameState.player.creature
    }
    override fun execute(event: JumpEvent) {
        println("You jump from ${event.source}")
        val damage = calculateJumpDamage(event)

        EventManager.postEvent(ArriveEvent(destination = event.destination, method = "fall"))
        EventManager.postEvent(StatChangeEvent(GameState.player.creature, "Falling", Stat.HEALTH, damage))
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