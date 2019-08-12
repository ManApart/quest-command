package system

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Player
import core.gameState.location.LocationPoint
import core.gameState.quests.QuestManager
import core.history.ChatHistory
import core.history.display
import core.utility.reflection.eventListeners
import interact.scope.ScopeManager
import system.item.ItemManager
import travel.ArriveEvent

object GameManager {
    var playing = false

    fun saveGame() {
        TODO("not implemented") //To change body of created functions interact File | Settings | File Templates.
    }

    fun loadGame() {
        TODO("not implemented") //To change body of created functions interact File | Settings | File Templates.
    }

    fun newGame() {
        ChatHistory.reset()
        GameState.reset()
        QuestManager.reset()
        ScopeManager.reset()
//        LocationManager.clear()

        eventListeners.forEach { it.reset() }

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