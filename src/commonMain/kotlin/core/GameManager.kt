package core

import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import core.thing.Thing
import core.thing.item.ItemManager
import core.thing.thing
import quests.QuestManager
import status.stat.*
import system.debug.DebugType
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

    suspend fun newOrLoadGame() {
        val gameMetaData = getGamesMetaData()
        if (gameMetaData.values.getBoolean(AUTO_LOAD) && getGameNames().isNotEmpty()) {
            val saveName = gameMetaData.values.getString(LAST_SAVE_GAME_NAME, getGameNames().first())
            EventManager.postEvent(LoadEvent(GameState.player, saveName))
        } else {
            newGame()
        }
    }

    suspend fun newGame(gameName: String = "Kanbara", playerName: String = "Player", testing: Boolean = false) {
        startupLog("Creating New Game.")
        //Set initial time to day
        GameState.timeManager.setTime(50)

        QuestManager.reset()
        LocationManager.reset()
        EventManager.reset()

        GameState.reset()
        setDefaultProperties(testing)
        GameState.gameName = gameName
        val player = newPlayer(playerName)
        GameState.putPlayer(player, true)
        CommandParsers.reset()
        GameLogger.reset()

        giveStartingItems(player.thing)
        EventManager.postEvent(ArriveEvent(player.thing, destination = LocationPoint(player.thing.location), method = "wake"))
        playing = true
        EventManager.postEvent(GameStartEvent())
    }

    private fun setDefaultProperties(testing: Boolean) {
        //        GameState.properties.values.put(AUTO_SAVE, true)
        GameState.properties.values.put(AUTO_LOAD, !testing)
        GameState.putDebug(DebugType.POLL_CONNECTION, !testing)
        GameState.properties.values.put(TEST_SAVE_FOLDER, testing)
        GameState.properties.values.put(SKIP_SAVE_STATS, testing)
        GameState.properties.values.put(PRINT_WITHOUT_FLUSH, testing)
        GameState.putDebug(DebugType.AI_Updates, testing)
    }

    suspend fun newPlayer(
        name: String = "Player",
        description: String = "Our Hero!",
        body: String = "Human",
        location: LocationNode = LocationManager.getNetwork(PLAYER_START_NETWORK).findLocation(PLAYER_START_LOCATION)
    ): Player {
        val player = thing(name){
            description(description)
            playerAI()
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
            addStat(SMITHING, 1)
            addStat(CRAFTSMANSHIP, 1)
        }

        with(player.properties.tags) {
            add("Open")
            add("Container")
            add("Creature")
        }

        return Player(name, player)
    }

    suspend fun giveStartingItems(player: Thing) {
        val inventory = player.inventory
        val body = player.body
        listOf("Brown Pants", "Old Shirt", "Rusty Dagger", "Small Pouch").forEach {
            val item = ItemManager.getItem(it)
            inventory.add(item)
            if (!body.isEquipped(item)) {
                body.equip(item)
            }
        }
        val pouch = inventory.getItem("Small Pouch")!!
        listOf("Apple").forEach {
            val item = ItemManager.getItem(it)
            pouch.inventory.add(item)
        }
    }

}