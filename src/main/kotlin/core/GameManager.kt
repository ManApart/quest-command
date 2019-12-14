package core

import core.commands.CommandParser
import core.events.EventListener
import core.events.EventManager
import core.target.Player
import traveling.location.LocationPoint
import quests.QuestManager
import core.history.ChatHistory
import core.history.SessionHistory
import core.history.display
import traveling.scope.ScopeManager
import core.target.item.ItemManager
import system.message.MessageEvent
import traveling.arrive.ArriveEvent

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
        GameState.reset()
//        LocationManager.clear()

//        GameState.properties.values.put(AUTO_SAVE, true)

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