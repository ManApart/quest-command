package system.persistance.createPlayer

import core.GameManager
import core.GameState
import core.events.EventListener
import core.history.display

class CreateCharacter : EventListener<CreateCharacterEvent>() {
    override fun execute(event: CreateCharacterEvent) {
        val player = GameManager.newPlayer(event.characterName)
        GameState.putPlayer(player)
        display("Created new Character ${event.characterName}.")
    }

}