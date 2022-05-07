package status.statChanged

import core.GameState
import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.Player
import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe
import core.history.displayToOthers
import inventory.dropItem.PlaceItemEvent
import status.stat.HEALTH
import status.stat.STAMINA
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationManager
import traveling.location.location.LocationPoint

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.thing.isPlayer()
    }

    override fun execute(event: StatMinnedEvent) {
        when (event.stat.lowercase()) {
            HEALTH.lowercase() -> GameState.getPlayer(event.thing)?.let { killPlayer(it) }
            STAMINA.lowercase() -> event.thing.displayToMe("You are completely exhausted.")
        }
    }

    private fun killPlayer(source: Player) {
        source.displayToMe("Oh dear, you have died!")
        source.displayToOthers("${source.name} has died!")

        with(source.thing) {
            location.getLocation().getCreatures()
                .filter { it.ai.aggroThing == this }
                .forEach { it.ai.aggroThing = null }

            inventory.getAllItems().forEach {
                EventManager.postEvent(PlaceItemEvent(this, it, silent = true))
            }

            ai.aggroThing = null
            soul.resetStatsAndConditions()

            val respawnNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION)
            EventManager.postEvent(ArriveEvent(this, destination = LocationPoint(respawnNode), method = "respawn"))

        }
    }
}