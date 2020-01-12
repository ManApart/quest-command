package system.persistance.loading

import core.GameState
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventListener
import core.history.display
import system.persistance.clean
import system.persistance.getGameNames

class Load : EventListener<LoadEvent>() {
    override fun execute(event: LoadEvent) {
        val gameName = clean(event.saveName)
        val allSaves = getGameNames()
        val saves = allSaves.filter { it.toLowerCase().contains(gameName.toLowerCase()) }
        val noMatchResponse = ResponseRequest("Could not find a match for $gameName. What game would you like to load?\n\t${allSaves.joinToString(", ")}", allSaves.map { it to "Load $it" }.toMap())
        val tooManyMatchesResponse = ResponseRequest("What game would you like to load?\n\t${saves.joinToString(", ")}", saves.map { it to "Load $it" }.toMap())
        when {
            saves.isEmpty() -> CommandParser.setResponseRequest(noMatchResponse)
            saves.size > 1 -> CommandParser.setResponseRequest(tooManyMatchesResponse)
            else -> loadGameAndPlayer(gameName)
        }
    }

    private fun loadGameAndPlayer(gameName: String) {
        system.persistance.loadGame(gameName)
        display("Now playing ${GameState.player.name} in ${GameState.gameName}.")
    }

}