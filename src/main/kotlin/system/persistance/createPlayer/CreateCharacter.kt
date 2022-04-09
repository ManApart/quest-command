package system.persistance.createPlayer

import core.GameManager
import core.GameState
import core.events.EventListener

class CreateCharacter : EventListener<CreateCharacterEvent>() {
    override fun execute(event: CreateCharacterEvent) {
        val player = GameManager.newPlayer(event.characterName, id = GameState.nextPlayerId())
        GameState.putPlayer(player)
    }

}