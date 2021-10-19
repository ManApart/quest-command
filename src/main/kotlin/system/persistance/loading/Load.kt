package system.persistance.loading

import core.GameState
import core.commands.CommandParsers
import core.commands.ResponseRequest
import core.events.EventListener
import core.history.displayToMe
import system.persistance.clean
import system.persistance.getGameNames

class Load : EventListener<LoadEvent>() {
    override fun execute(event: LoadEvent) {
        val gameName = clean(event.saveName).removeSuffix("/")
        val allSaves = getGameNames()
        val saves = allSaves.filter { it.lowercase().contains(gameName.lowercase()) }
        val noMatchResponse = ResponseRequest("Could not find a match for $gameName. What game would you like to load?\n\t${allSaves.joinToString(", ")}",
            allSaves.associateWith { "Load $it" })
        val tooManyMatchesResponse = ResponseRequest("What game would you like to load?\n\t${saves.joinToString(", ")}",
            saves.associateWith { "Load $it" })
        when {
            saves.isEmpty() -> CommandParsers.setResponseRequest(event.source, noMatchResponse)
            saves.size > 1 -> CommandParsers.setResponseRequest(event.source, tooManyMatchesResponse)
            else -> loadGameAndPlayer(gameName)
        }
    }

    private fun loadGameAndPlayer(gameName: String) {
        system.persistance.loadGame(gameName)
        GameState.player.thing.displayToMe("Now playing ${GameState.player.thing.name} in ${GameState.gameName}.")
    }

}