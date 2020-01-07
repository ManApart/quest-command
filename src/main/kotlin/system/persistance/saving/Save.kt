package system.persistance.saving

import com.fasterxml.jackson.databind.ObjectMapper
import core.GameState
import core.events.EventListener
import core.target.getPersisted
import java.io.File

class Save : EventListener<SaveEvent>() {
    private val directory = "./saves/"

    override fun execute(event: SaveEvent) {
//        SessionHistory.saveSessionStats()
        val playerData = getPersisted(GameState.player)
        val saveName = generateSaveName(GameState.player.givenName)
        writeSave(saveName, playerData)

        println("Saved to $saveName.")
    }

    private fun generateSaveName(name: String): String {
        return directory + name.replace(" ", "_").replace(Regex("[^a-zA-Z]"), "") + ".json"
    }

    private fun writeSave(playerSavePath: String, data: Map<String, Any>) {
        val directory = File(directory)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val json = ObjectMapper().writeValueAsString(data)
        File(playerSavePath).printWriter().use { out ->
            out.println(json)
        }
    }
}