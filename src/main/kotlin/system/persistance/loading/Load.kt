package system.persistance.loading

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.GameState
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventListener
import core.history.display
import core.target.readFromData
import traveling.scope.ScopeManager
import java.io.File

class Load : EventListener<LoadEvent>() {
    override fun execute(event: LoadEvent) {
        when {
            event.list -> listSaves()
            !event.saveName.isNullOrBlank() -> attemptLoad(event.saveName)
            else -> println("Could not find a save to load.")
        }
    }

    private fun listSaves() {
        val saveNames = getSaveNames()
        display("Saves:\n\t" + saveNames.joinToString("\n\t"))

    }

    private fun getSaveNames(): List<String> {
        return File("./saves/").listFiles().map { it.name.substring(0, it.name.length - ".json".length) }
    }

    private fun attemptLoad(playerGivenSaveName: String) {
        val allSaves = getSaveNames()
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
        val playerData = readSave("./saves/$saveName.json")

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