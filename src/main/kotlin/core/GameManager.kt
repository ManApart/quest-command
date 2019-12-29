package core

import core.commands.CommandParser
import core.events.EventListener
import core.events.EventManager
import core.history.ChatHistory
import core.history.SessionHistory
import core.history.display
import core.target.Player
import core.target.item.ItemManager
import quests.QuestManager
import status.stat.*
import system.message.MessageEvent
import traveling.arrive.ArriveEvent
import traveling.location.LocationPoint
import traveling.scope.ScopeManager

const val PLAYER_START_NETWORK = "Kanbara Countryside"
const val PLAYER_START_LOCATION = "An Open Field"

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

        GameState.player = newPlayer()
        giveStartingItems(GameState.player)
        EventManager.postEvent(ArriveEvent(destination = LocationPoint(GameState.player.location), method = "wake"))
        playing = true
        EventManager.postEvent(GameStartEvent())
    }

    fun newPlayer(): Player {
        val player = Player()
        with(player.soul) {
            addStat(HEALTH, 1, 10, 1)
            addStat(PERCEPTION, 1, 1, 1)
            addStat(STAMINA, 1, 100, 1)
            addStat(FOCUS, 1, 100, 1)
            addStat(STRENGTH, 1, 1, 1)
            addStat(WISDOM, 1, 1, 1)
            addStat(CLIMBING, 1)
            addStat(AGILITY, 1)
            addStat(COOKING, 1)
        }

        with(player.properties) {
            tags.add("Open")
            tags.add("Container")
            tags.add("Creature")
        }

        return player
    }

    private fun giveStartingItems(player: Player) {
        val inventory = player.inventory
        val body = player.body
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