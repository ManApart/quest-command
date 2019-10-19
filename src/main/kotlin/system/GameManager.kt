package system

import core.commands.CommandParser
import core.events.EventListener
import core.gameState.GameState
import core.gameState.Player
import core.gameState.location.LocationPoint
import core.gameState.quests.QuestManager
import core.history.ChatHistory
import core.history.SessionHistory
import core.history.display
import interact.scope.ScopeManager
import system.item.ItemManager
import travel.ArriveEvent

object GameManager {
    var playing = false

    fun saveGame() {
        SessionHistory.saveSessionStats()
    }

    fun loadGame() {
        TODO("not implemented") //To change body of created functions interact File | Settings | File Templates.
    }

    fun newGame() {
        CommandParser.reset()
        ChatHistory.reset()
        GameState.reset()
        QuestManager.reset()
        ScopeManager.reset()
        EventManager.reset()
//        LocationManager.clear()

        newPlayer()
        EventManager.postEvent(ArriveEvent(destination = LocationPoint(GameState.player.location), method = "wake"))
        playing = true
        EventManager.postEvent(GameStartEvent())
    }

    private fun newPlayer() {
        GameState.player = Player()
        val inventory = GameState.player.inventory
        val body = GameState.player.body

        listOf("Brown Pants", "Old Shirt", "Rusty Dagger", "Small Pouch").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
            body.equip(item)
        }
        listOf("Apple").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
        }
    }

    class MessageHandler : EventListener<MessageEvent>() {
        override fun execute(event: MessageEvent) {
            display(event.message)
        }

    }

}