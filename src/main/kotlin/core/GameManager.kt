package core

import core.commands.CommandParser
import core.conditional.ConditionalStringPointer
import core.events.EventManager
import core.history.ChatHistory
import core.target.Target
import core.target.item.ItemManager
import quests.QuestManager
import status.stat.*
import system.persistance.getGameNames
import system.persistance.getGamesMetaData
import system.persistance.loading.LoadEvent
import system.startup.GameStartEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint

const val PLAYER_START_NETWORK = "Kanbara Countryside"
const val PLAYER_START_LOCATION = "An Open Field"


object GameManager {
    var playing = false

    fun newOrLoadGame() {
        val gameMetaData = getGamesMetaData()
        if (gameMetaData.values.getBoolean(AUTO_LOAD) && getGameNames().isNotEmpty()) {
            val saveName = gameMetaData.values.getString(LAST_SAVE_GAME_NAME, getGameNames().first())
            EventManager.postEvent(LoadEvent(saveName))
        } else {
            newGame()
        }
    }

    fun newGame(gameName: String = "Kanbara", playerName: String = "Player", testing: Boolean = false) {
        CommandParser.reset()
        ChatHistory.reset()
        GameState.reset()
        QuestManager.reset()
        LocationManager.reset()
        EventManager.reset()
        GameState.reset()
//        LocationManager.clear()
        setDefaultProperties(testing)

        GameState.gameName = gameName
        GameState.player = newPlayer(playerName)
        giveStartingItems(GameState.player)
        EventManager.postEvent(ArriveEvent(destination = LocationPoint(GameState.player.location), method = "wake"))
        playing = true
        EventManager.postEvent(GameStartEvent())
    }

    private fun setDefaultProperties(testing: Boolean) {
        //        GameState.properties.values.put(AUTO_SAVE, true)
        GameState.properties.values.put(AUTO_LOAD, !testing)
        GameState.properties.values.put(SKIP_SAVE_STATS, testing)
    }

    fun newPlayer(
            name: String = "Player",
            dynamicDescription: ConditionalStringPointer = ConditionalStringPointer("Our Hero!"),
            body: String = "Human",
            location: LocationNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION)
    ): Target {
        val player = Target(name = name, dynamicDescription = dynamicDescription, aiName = core.ai.PLAYER_CONTROLLED_ID, bodyName = body, location = location)

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

        with(player.properties.tags) {
            add("Open")
            add("Container")
            add("Creature")
        }

        return player
    }

    private fun giveStartingItems(player: Target) {
        val inventory = player.inventory
        val body = player.body
        listOf("Brown Pants", "Old Shirt", "Rusty Dagger", "Small Pouch").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
            if (!body.isEquipped(item)) {
                body.equip(item)
            }
        }
        listOf("Apple").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
        }
    }

}