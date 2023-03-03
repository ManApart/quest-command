package system.persistance.changePlayer

import core.GameState
import core.Player
import core.commands.respond
import core.events.EventListener
import core.history.TerminalPrinter
import core.history.displayGlobal
import core.history.displayToMe
import system.persistance.clean
import system.persistance.getCharacterSaves

class PlayAs : EventListener<PlayAsEvent>() {
    override suspend fun complete(event: PlayAsEvent) {
        val newCharacter = GameState.players.values.firstOrNull { it.name.lowercase() == event.characterName.lowercase() } ?: loadCharacter(event.source, GameState.gameName, event.characterName)
        if (newCharacter != null) {
            GameState.player = newCharacter
            TerminalPrinter.reset()
            displayGlobal("${event.source.name} is now playing ${newCharacter.name} in ${GameState.gameName}.")
        }

    }

    //TODO - I don't think loading a character makes sense
    private suspend fun loadCharacter(source: Player, gameName: String, characterName: String): Player? {
        val playerSaveName = clean(characterName)
        val allSaves = getCharacterSaves(gameName)
        val saves = allSaves.filter { it.lowercase().contains(playerSaveName.lowercase()) }
        when {
            allSaves.isEmpty() -> {
                source.displayToMe("Could not find a save for $characterName")
                return null
            }
            saves.isEmpty() -> source.respond("No characters found to play.") {
                message("Could not find a match for $playerSaveName. What character would you like to play?")
                options(allSaves)
                command { "be $it" }
            }
            saves.size > 1 -> source.respond("No characters found to play.") {
                message("What character would you like to play?")
                options(saves)
                command { "be $it" }
            }
            else -> return system.persistance.loadCharacter(gameName, saves.first(), source.name)
        }
        return null
    }


}