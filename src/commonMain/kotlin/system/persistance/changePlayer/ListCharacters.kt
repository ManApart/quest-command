package system.persistance.changePlayer

import core.GameState
import core.events.EventListener
import core.history.displayToMe
import core.utility.highlightCurrent
import system.persistance.clean
import system.persistance.getCharacterSaves

class ListCharacters : EventListener<ListCharactersEvent>() {
    override suspend fun execute(event: ListCharactersEvent) {
        val playerNames = GameState.players.values.map { it.name }
        if (playerNames.isEmpty()) {
            event.source.displayToMe("No characters to play as.")
        } else {
            event.source.displayToMe("Characters in ${GameState.gameName}:\n\t" + playerNames.joinToString("\n\t") { it.highlightCurrent(event.source.name) })
        }
    }

}