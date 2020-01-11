package system.persistance.changePlayer

import core.GameState
import core.events.EventListener
import core.history.display
import system.persistance.getCharacterSaves

class ListCharacters : EventListener<ListCharactersEvent>() {
    override fun execute(event: ListCharactersEvent) {
        val saveNames = getCharacterSaves(GameState.gameName)
        display("Characters in ${GameState.gameName}:\n\t" + saveNames.joinToString("\n\t"))
    }
}