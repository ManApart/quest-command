package system.persistance.changePlayer

import core.GameState
import core.Player
import core.commands.respond
import core.events.EventListener
import core.history.displayToMe
import system.persistance.clean
import system.persistance.getCharacterSaves
import system.persistance.save

class PlayAs : EventListener<PlayAsEvent>() {
    override fun execute(event: PlayAsEvent) {
        save(GameState.gameName, event.source)
        loadCharacter(event.source, GameState.gameName, event.saveName)
        event.source.displayToMe("Now playing ${event.source.thing.name} in ${GameState.gameName}.")
    }

    private fun loadCharacter(source: Player, gameName: String, characterName: String) {
        val playerSaveName = clean(characterName)
        val allSaves = getCharacterSaves(gameName)
        val saves = allSaves.filter { it.lowercase().contains(playerSaveName.lowercase()) }
        when {
            saves.isEmpty() -> source.respond {
                message("Could not find a match for $playerSaveName. What character would you like to play?")
                options(allSaves)
                command { "be $it" }
            }
            saves.size > 1 -> source.respond {
                message("What character would you like to play?")
                options(saves)
                command { "be $it" }
            }
            else -> system.persistance.loadCharacter(gameName, saves.first())
        }
    }


}