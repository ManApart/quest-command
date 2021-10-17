package core

import core.ai.PLAYER_CONTROLLED_ID
import core.commands.CommandParser
import core.events.EventManager
import core.history.GameLogger
import core.target.Target
import core.target.item.ItemManager
import core.target.target
import quests.QuestManager
import status.stat.*
import system.persistance.getGameNames
import system.persistance.getGamesMetaData
import system.persistance.loading.LoadEvent
import system.startup.GameStartEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationManager
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode

const val PLAYER_START_NETWORK = "Kanbara Countryside"
const val PLAYER_START_LOCATION = "An Open Field"


object GameManager {
    var playing = false

    fun newOrLoadGame() {
        val gameMetaData = getGamesMetaData()
        if (gameMetaData.values.getBoolean(AUTO_LOAD) && getGameNames().isNotEmpty()) {
            val saveName = gameMetaData.values.getString(LAST_SAVE_GAME_NAME, getGameNames().first())
            EventManager.postEvent(LoadEvent(GameState.player, saveName))
        } else {
            newGame()
        }
    }

    fun newGame(gameName: String = "Kanbara", playerName: String = "Player", testing: Boolean = false) {
        GameState.reset()
        QuestManager.reset()
        LocationManager.reset()
        EventManager.reset()
        GameState.reset()
//        LocationManager.clear()
        setDefaultProperties(testing)

        GameState.gameName = gameName
        GameState.player = newPlayer(playerName)
        CommandParser.reset()
        GameLogger.reset()

        giveStartingItems(GameState.player.target)
        EventManager.postEvent(ArriveEvent(GameState.player.target, destination = LocationPoint(GameState.player.target.location), method = "wake"))
        playing = true
        EventManager.postEvent(GameStartEvent())
    }

    private fun setDefaultProperties(testing: Boolean) {
        //        GameState.properties.values.put(AUTO_SAVE, true)
        GameState.properties.values.put(AUTO_LOAD, !testing)
        GameState.properties.values.put(SKIP_SAVE_STATS, testing)
        GameState.properties.values.put(PRINT_WITHOUT_FLUSH, testing)
    }

    fun newPlayer(
        name: String = "Player",
        description: String = "Our Hero!",
        body: String = "Human",
        location: LocationNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION)
    ): Player {
        val player = target(name){
            description(description)
            ai(PLAYER_CONTROLLED_ID)
            body(body)
            location(location)
        }.build()

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

        return Player(0, player)
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