package system.persistance.loading

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.GameState
import core.events.EventListener
import core.target.readFromData
import traveling.scope.ScopeManager
import java.io.File

class Load : EventListener<LoadEvent>() {
    private val playerSavePath = "./saves/PlayerSave.json"
    override fun execute(event: LoadEvent) {
        val playerData = readSave()

        ScopeManager.getScope().removeTarget(GameState.player)
        GameState.player = readFromData(playerData)
        ScopeManager.getScope().addTarget(GameState.player)


        println("Loaded!")
    }

    private fun readSave(): Map<String, Any> {
        val stream = File(playerSavePath)
        return jacksonObjectMapper().readValue(stream)
    }
}