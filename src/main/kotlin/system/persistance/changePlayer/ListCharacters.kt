package system.persistance.changePlayer

import core.GameState
import core.events.EventListener
import core.history.display
import system.persistance.clean
import system.persistance.getCharacterSaves

class ListCharacters : EventListener<ListCharactersEvent>() {
    override fun execute(event: ListCharactersEvent) {
        val saveNames = getCharacterSaves(GameState.gameName)
        if (saveNames.isEmpty()) {
            display("No characters to play as.")
        } else {
            display("Characters in ${GameState.gameName}:\n\t" + saveNames.joinToString("\n\t") { highlightCurrent(it, GameState.player.name) })
        }
    }

    private fun highlightCurrent(save: String, currentSave: String): String {
        return if (save == clean(currentSave)) {
            "*$currentSave"
        } else {
            save
        }
    }

}