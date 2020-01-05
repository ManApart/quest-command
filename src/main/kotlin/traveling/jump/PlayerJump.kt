package traveling.jump

import core.events.EventListener
import core.GameState
import traveling.direction.NO_VECTOR
import traveling.location.location.LocationPoint
import status.stat.AGILITY
import status.stat.HEALTH
import core.history.display
import status.statChanged.StatChangeEvent
import core.events.EventManager
import traveling.arrive.ArriveEvent

class PlayerJump : EventListener<JumpEvent>() {
    override fun shouldExecute(event: JumpEvent): Boolean {
        return event.creature == GameState.player
    }
    override fun execute(event: JumpEvent) {
        display("You jump from ${event.source}")
        val damage = calculateJumpDamage(event)

        GameState.player.finishClimbing()

        if (damage != 0) {
            EventManager.postEvent(StatChangeEvent(GameState.player, "Falling", HEALTH, damage))
        } else {
            display("You land without taking damage.")
        }

        EventManager.postEvent(ArriveEvent(destination = LocationPoint(event.destination), method = "fall"))
    }

    private fun calculateJumpDamage(event: JumpEvent): Int {
        val soul = event.creature.soul
        val position = event.source.getConnection(event.destination)?.vector ?: NO_VECTOR
        val height = event.fallDistance ?: Math.abs(position.z)
        val damage = height - 2*soul.getCurrent(AGILITY)

        //TODO - look at foot defense and factor that in
        //TODO - factor in encumbrance. The higher the encumbrance, the greater the damage
        return -Math.max(damage, 0)
    }
}