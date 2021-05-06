package system.persistance.changePlayer

import core.GameState
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventListener
import core.history.display
import system.persistance.clean
import system.persistance.getCharacterSaves
import system.persistance.save

class PlayAs : EventListener<PlayAsEvent>() {
    override fun execute(event: PlayAsEvent) {
        save(GameState.gameName, GameState.player)
        loadCharacter(GameState.gameName, event.saveName)
        display("Now playing ${GameState.player.name} in ${GameState.gameName}.")
    }

    private fun loadCharacter(gameName: String, characterName: String) {
        val playerSaveName = clean(characterName)
        val allSaves = getCharacterSaves(gameName)
        val saves = allSaves.filter { it.lowercase().contains(playerSaveName.lowercase()) }
        val noMatchResponse = ResponseRequest("Could not find a match for $playerSaveName. What character would you like to play?\n\t${allSaves.joinToString(", ")}",
            allSaves.associateWith { "Be $it" })
        val tooManyMatchesResponse = ResponseRequest("What character would you like to play?\n\t${saves.joinToString(", ")}",
            saves.associateWith { "Be $it" })
        when {
            saves.isEmpty() -> CommandParser.setResponseRequest(noMatchResponse)
            saves.size > 1 -> CommandParser.setResponseRequest(tooManyMatchesResponse)
            else -> system.persistance.loadCharacter(gameName, saves.first())
        }
    }


}