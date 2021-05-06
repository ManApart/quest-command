package traveling.jump

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.display
import status.stat.AGILITY
import status.stat.HEALTH
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint
import traveling.position.NO_VECTOR
import kotlin.math.abs
import kotlin.math.max

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
        val height = event.fallDistance ?: abs(position.z)
        val damage = height - 2*soul.getCurrent(AGILITY)

        //TODO - look at foot defense and factor that in
        //TODO - factor in encumbrance. The higher the encumbrance, the greater the damage
        return -max(damage, 0)
    }
}