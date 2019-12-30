package system.persistance.saving

import com.fasterxml.jackson.databind.ObjectMapper
import core.GameState
import core.events.EventListener
import core.target.getPersisted
import java.io.File

class Save : EventListener<SaveEvent>() {
    private val directory = "./saves/"
    private val playerSavePath = "./saves/PlayerSave.json"

    override fun execute(event: SaveEvent) {
//        SessionHistory.saveSessionStats()
        val playerData = getPersisted(GameState.player)
        writeSave(playerData)

        println("Saved!")
    }

    private fun writeSave(data: Map<String, Any>) {
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