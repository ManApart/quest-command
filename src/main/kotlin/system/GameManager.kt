package system

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Player
import core.gameState.quests.QuestManager
import core.history.ChatHistory
import core.history.display
import system.location.LocationManager
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
//        LocationManager.reset()

        newPlayer()
        EventManager.postEvent(ArriveEvent(destination = GameState.player.creature.location, method = "wake"))
        playing = true
        EventManager.postEvent(GameStartEvent())
    }

    private fun newPlayer() {
        GameState.player = Player()
        val inventory = GameState.player.creature.inventory
        val body = GameState.player.creature.body

        listOf("Brown Pants", "Old Shirt", "Rusty Dagger").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
            body.equip(item)
        }
        listOf("Tinder Box", "Dulled Hatchet", "Apple", "Apple Pie").forEach {
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