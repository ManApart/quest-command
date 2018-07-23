package travel.jump

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.Stat
import status.statChanged.StatChangeEvent
import system.EventManager
import travel.ArriveEvent

class PlayerJump : EventListener<JumpEvent>() {
    override fun shouldExecute(event: JumpEvent): Boolean {
        return event.creature == GameState.player.creature
    }
    override fun execute(event: JumpEvent) {
        println("You jump from ${event.source}")
        val damage = calculateJumpDamage(event)

        GameState.finishJourney()

        if (damage != 0) {
            EventManager.postEvent(StatChangeEvent(GameState.player.creature, "Falling", Stat.HEALTH, damage))
        } else {
            println("You land without taking damage.")
        }

        EventManager.postEvent(ArriveEvent(destination = event.destination, method = "fall"))


    }

    private fun calculateJumpDamage(event: JumpEvent): Int {
        val soul = event.creature.soul
        val height = event.fallDistance ?: Math.abs(event.source.position.z - event.destination.position.z)
        val damage = height - 2*soul.getCurrent(Stat.AGILITY)

        //TODO - look at foot defense and factor that in
        //TODO - factor in encumberance. The higher the encumberance, the greater the damage
        return -Math.max(damage, 0)
    }
}