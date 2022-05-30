package system.persistance.loading

import core.GameState
import core.commands.respond
import core.events.EventListener
import core.history.displayToMe
import system.persistance.clean
import system.persistance.getGameNames

class Load : EventListener<LoadEvent>() {
    override fun execute(event: LoadEvent) {
        val gameName = clean(event.saveName).removeSuffix("/")
        val allSaves = getGameNames()
        val saves = allSaves.filter { it.lowercase().contains(gameName.lowercase()) }

        when {
            saves.isEmpty() -> event.source.respond("No saves found.") {
                message("Could not find a match for $gameName. What game would you like to load?")
                options(allSaves)
                command { "load $it" }
            }
            saves.size > 1 -> event.source.respond("No saves found.") {
                message("What game would you like to load?")
                options(saves)
                command { "load $it" }
            }
            else -> loadGameAndPlayer(gameName)
        }
    }

    private fun loadGameAndPlayer(gameName: String) {
        system.persistance.loadGame(gameName)
        GameState.player.thing.displayToMe("Now playing ${GameState.player.thing.name} in ${GameState.gameName}.")
    }

}