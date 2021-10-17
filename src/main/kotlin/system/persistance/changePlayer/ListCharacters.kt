package system.persistance.changePlayer

import core.GameState
import core.events.EventListener
import core.history.displayToMe
import system.persistance.clean
import system.persistance.getCharacterSaves

class ListCharacters : EventListener<ListCharactersEvent>() {
    override fun execute(event: ListCharactersEvent) {
        val saveNames = getCharacterSaves(GameState.gameName)
        if (saveNames.isEmpty()) {
            event.source.displayToMe("No characters to play as.")
        } else {
            event.source.displayToMe("Characters in ${GameState.gameName}:\n\t" + saveNames.joinToString("\n\t") { highlightCurrent(it, event.source.target.name) })
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