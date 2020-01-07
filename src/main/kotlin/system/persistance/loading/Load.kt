package system.persistance.loading

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.GameState
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventListener
import core.history.display
import core.target.readFromData
import system.persistance.clean
import system.persistance.directory
import system.persistance.getSaveNames
import traveling.scope.ScopeManager
import java.io.File

class Load : EventListener<LoadEvent>() {
    override fun execute(event: LoadEvent) {
        if (event.loadGame) {
            display("Not implemented yet")
        } else {
            loadCharacterSave(event.saveName)
        }
    }

    private fun loadCharacterSave(saveName: String) {
        val playerGivenSaveName = clean(saveName)
        val allSaves = getSaveNames(GameState.gameName)
        val saves = allSaves.filter { it.contains(playerGivenSaveName) }
        val noMatchResponse = ResponseRequest("Could not find a match for $playerGivenSaveName. What save would you like to load?\n\t${allSaves.joinToString(", ")}", allSaves.map { it to "Load $it" }.toMap())
        val tooManyMatchesResponse = ResponseRequest("What save would you like to load?\n\t${saves.joinToString(", ")}", saves.map { it to "Load $it" }.toMap())
        when {
            saves.isEmpty() -> CommandParser.setResponseRequest(noMatchResponse)
            saves.size > 1 -> CommandParser.setResponseRequest(tooManyMatchesResponse)
            else -> loadSave(saves.first())
        }
    }

    private fun loadSave(saveName: String) {
        val playerData = readSave("$directory/${GameState.gameName}/$saveName.json")

        ScopeManager.getScope().removeTarget(GameState.player)
        GameState.player = readFromData(playerData)
        ScopeManager.getScope().addTarget(GameState.player)

        println("Loaded $saveName.")
    }

    private fun readSave(playerSavePath: String): Map<String, Any> {
        val stream = File(playerSavePath)
        return jacksonObjectMapper().readValue(stream)
    }
}