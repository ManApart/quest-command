package system.persistance.saving

import com.fasterxml.jackson.databind.ObjectMapper
import core.GameState
import core.events.EventListener
import java.io.File

class Save : EventListener<SaveEvent>() {
    private val directory = "./saves/"
    private val playerSavePath = "./saves/PlayerSave.json"

    override fun execute(event: SaveEvent) {
//        SessionHistory.saveSessionStats()
        val playerData = GameState.player.getPersisted()
        writeSave(playerSavePath, playerData)

        println("Saved!")
    }

    private fun writeSave(savePath: String, data: Map<String, Any>) {
        val directory = File(directory)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val json = ObjectMapper().writeValueAsString(data)
        File(savePath).printWriter().use { out ->
            out.println(json)
        }
    }
}