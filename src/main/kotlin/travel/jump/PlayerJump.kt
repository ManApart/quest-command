package travel.jump

import core.events.EventListener
import core.gameState.GameState
import core.gameState.NO_POSITION
import core.gameState.stat.Stat
import core.history.display
import status.statChanged.StatChangeEvent
import system.EventManager
import travel.ArriveEvent

class PlayerJump : EventListener<JumpEvent>() {
    override fun shouldExecute(event: JumpEvent): Boolean {
        return event.creature == GameState.player.creature
    }
    override fun execute(event: JumpEvent) {
        display("You jump from ${event.source}")
        val damage = calculateJumpDamage(event)

        GameState.player.finishJourney()

        if (damage != 0) {
            EventManager.postEvent(StatChangeEvent(GameState.player.creature, "Falling", Stat.HEALTH, damage))
        } else {
            display("You land without taking damage.")
        }

        EventManager.postEvent(ArriveEvent(destination = event.destination, method = "fall"))
    }

    private fun calculateJumpDamage(event: JumpEvent): Int {
        val soul = event.creature.soul
        val position = event.source.getLink(event.destination)?.position ?: NO_POSITION
        val height = event.fallDistance ?: Math.abs(position.z)
        val damage = height - 2*soul.getCurrent(Stat.AGILITY)

        //TODO - look at foot defense and factor that in
        //TODO - factor in encumbrance. The higher the encumbrance, the greater the damage
        return -Math.max(damage, 0)
    }
}