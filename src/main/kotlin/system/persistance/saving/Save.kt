package system.persistance.saving

import com.fasterxml.jackson.databind.ObjectMapper
import core.GameState
import core.events.EventListener
import core.target.getPersisted
import system.persistance.clean
import system.persistance.directory
import java.io.File

class Save : EventListener<SaveEvent>() {
    override fun execute(event: SaveEvent) {
//        SessionHistory.saveSessionStats()
        val playerData = getPersisted(GameState.player)
        val saveName = generateSaveName(GameState.gameName, GameState.player.name)
        writeSave(clean(GameState.gameName), saveName, playerData)

        println("Saved to $saveName.")
    }

    private fun generateSaveName(gameName: String, playerName: String): String {
        return directory + clean(gameName) + "/" + clean(playerName) + ".json"
    }

    private fun writeSave(gameName: String, saveName: String, data: Map<String, Any>) {
        val directory = File("$directory$gameName/")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val json = ObjectMapper().writeValueAsString(data)
        File(saveName).printWriter().use { out ->
            out.println(json)
        }
    }
}