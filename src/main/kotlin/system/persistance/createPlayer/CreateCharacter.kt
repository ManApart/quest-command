package system.persistance.createPlayer

import core.GameManager
import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.display
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint

class CreateCharacter : EventListener<CreateCharacterEvent>() {
    override fun execute(event: CreateCharacterEvent) {
        val player = GameManager.newPlayer(event.characterName)
        GameManager.giveStartingItems(player.thing)
        EventManager.postEvent(ArriveEvent(player.thing, destination = LocationPoint(player.thing.location), method = "wake"))
        GameState.putPlayer(player)
        display("Created new Character ${event.characterName}.")
    }

}