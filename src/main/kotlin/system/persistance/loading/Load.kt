package system.persistance.loading

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.GameState
import core.events.EventListener
import core.target.Player
import java.io.File

class Load : EventListener<LoadEvent>() {
    private val playerSavePath = "./saves/PlayerSave.json"
    override fun execute(event: LoadEvent) {
        val playerData = readSave()
        GameState.player = Player()
        GameState.player.applyData(playerData)

        println("Loaded!")
    }

    private fun readSave(): Map<String, Any> {
        val stream = File(playerSavePath)
        return jacksonObjectMapper().readValue(stream)
    }
}