package status.statChanged

import core.GameState
import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.Player
import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe
import core.history.displayToOthers
import core.utility.filterList
import inventory.dropItem.PlaceItemEvent
import status.stat.HEALTH
import status.stat.STAMINA
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationManager
import traveling.location.location.LocationPoint

class PlayerStatMinned : EventListener<StatMinnedEvent>() {
    override suspend fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.thing.isPlayer()
    }

    override suspend fun complete(event: StatMinnedEvent) {
        when (event.stat.lowercase()) {
            HEALTH.lowercase() -> GameState.getPlayer(event.thing)?.let { killPlayer(it) }
            STAMINA.lowercase() -> event.thing.displayToMe("You are completely exhausted.")
        }
    }

    private suspend fun killPlayer(source: Player) {
        source.displayToMe("Oh dear, you have died!")
        source.displayToOthers("${source.name} has died!")

        EventManager.removeInProgressEvents(source.thing)
        with(source.thing) {
            mind.ai.actions.clear()
            location.getLocation().getCreatures()
                .filterList { it.mind.getAggroTarget() == this }
                .forEach { it.mind.clearAggroTarget() }

            inventory.getAllItems().forEach {
                EventManager.postEvent(PlaceItemEvent(this, it, silent = true, timeLeft = 0))
            }

            mind.clearAggroTarget()
            soul.resetStatsAndConditions()

            val respawnNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION)
            EventManager.postEvent(ArriveEvent(this, destination = LocationPoint(respawnNode), method = "respawn"))

        }
    }
}