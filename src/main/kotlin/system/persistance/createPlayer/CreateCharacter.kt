package system.persistance.createPlayer

import core.GameManager
import core.GameState
import core.events.EventListener

class CreateCharacter : EventListener<CreateCharacterEvent>() {
    override fun execute(event: CreateCharacterEvent) {
        val player = GameManager.newPlayer(event.characterName, id = nextPlayerId())
        GameState.putPlayer(player)
    }

    private fun nextPlayerId(): Int {
        return GameState.players.keys.maxOf { it } + 1
    }

}