package traveling.jump

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.asSubject
import explore.listen.addSoundEffect
import status.stat.Attributes.AGILITY
import status.stat.Attributes.HEALTH
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint
import traveling.position.NO_VECTOR
import kotlin.math.abs
import kotlin.math.max

class PlayerJump : EventListener<JumpEvent>() {
    override suspend fun shouldExecute(event: JumpEvent): Boolean {
        return event.creature.isPlayer()
    }
    override suspend fun complete(event: JumpEvent) {
        event.creature.display{"${event.creature.asSubject(it)} jump from ${event.source}"}
        val damage = calculateJumpDamage(event)

        event.creature.finishClimbing()

        if (damage != 0) {
            EventManager.postEvent(StatChangeEvent(event.creature, "Falling", HEALTH, damage))
        } else {
            event.creature.display{"${event.creature.asSubject(it)} land without taking damage."}
        }

        event.creature.addSoundEffect("Jumping", "a sharp thud", 20)
        EventManager.postEvent(ArriveEvent(event.creature, destination = LocationPoint(event.destination), method = "fall"))
    }

    private fun calculateJumpDamage(event: JumpEvent): Int {
        val soul = event.creature.soul
        val position = event.source.getConnection(event.destination)?.source?.vector ?: NO_VECTOR
        val height = event.fallDistance ?: abs(position.z)
        val damage = height - 2*soul.getCurrent(AGILITY)

        //TODO - look at foot defense and factor that in
        //TODO - factor in encumbrance. The higher the encumbrance, the greater the damage
        return -max(damage, 0)
    }
}