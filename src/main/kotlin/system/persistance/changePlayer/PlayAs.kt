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
        val name = event.source.name
        //Do we need to save?
        save(GameState.gameName)
        val newCharacter = GameState.players.values.firstOrNull { it.thing.name.lowercase() == event.characterName.lowercase() }
        if (newCharacter != null) {
            //Remove both old references, set the new character's id to match the caller
            GameState.players.remove(name)
            GameState.players.remove(newCharacter.name)
            GameState.putPlayer(newCharacter.copy(name = name))
        } else {
            loadCharacter(event.source, GameState.gameName, event.characterName)
        }
        val selected = GameState.getPlayer(name)!!

        selected.displayToMe("Now playing ${selected.thing.name} in ${GameState.gameName}.")
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
            else -> system.persistance.loadCharacter(gameName, saves.first(), source.name)
        }
    }


}