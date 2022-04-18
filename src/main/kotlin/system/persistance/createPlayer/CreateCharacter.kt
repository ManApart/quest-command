package system.persistance.createPlayer

import core.GameManager
import core.GameState
import core.events.EventListener
import core.events.EventManager
import system.persistance.changePlayer.PlayAsEvent

class CreateCharacter : EventListener<CreateCharacterEvent>() {
    override fun execute(event: CreateCharacterEvent) {
        val player = GameManager.newPlayer(event.characterName)
        GameState.putPlayer(player)
        EventManager.postEvent(PlayAsEvent(event.source, event.characterName))
    }

}