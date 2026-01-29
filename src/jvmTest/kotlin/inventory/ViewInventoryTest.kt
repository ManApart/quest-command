package inventory

import core.DependencyInjector
import core.GameState
import core.body.*
import core.history.GameLogger
import core.properties.Properties
import core.properties.TagKey.ITEM
import core.properties.Tags
import core.thing.Thing
import core.thing.thing
import createClosedChest
import createItem
import createMockedGame
import kotlinx.coroutines.runBlocking
import system.debug.DebugType
import traveling.location.location.locationRecipe
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ViewInventoryTest {

    @BeforeTest
    fun setup() {
        createMockedGame()
        GameState.putDebug(DebugType.CLARITY, true)
    }

    @Test
    fun listInventory() {
        runBlocking {
            val creature = createClosedChest()
            creature.inventory.add(createItem())
            val event = ViewInventoryEvent(GameState.player, creature)
            runBlocking { ViewInventory().complete(event) }
            assertTrue(GameLogger.getMainHistory().contains("Closed Chest has:\n\tApple"))
        }
    }

    @Test
    fun listNoItemsInventory() {
        val creature = createClosedChest()
        val event = ViewInventoryEvent(GameState.player, creature)
        runBlocking { ViewInventory().complete(event) }
        assertTrue(GameLogger.getMainHistory().contains("Closed Chest has no items."))
    }

    @Test
    fun listInventoryEquipped() {
        runBlocking {
            val chest = locationRecipe("chest") { slot("chest") }

            DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart(chest))
            DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock.fromPart(chest))
            BodyManager.reset()

            val creature = thing("Soldier") {
                body("body")
                props { tag("Container") }
            }.build()
            val item = Thing("Chestplate", equipSlots = listOf(Slot(listOf("Chest"))), properties = Properties(tags = Tags(ITEM)))
            creature.inventory.add(item)
            creature.body.equip(item)
            val event = ViewInventoryEvent(GameState.player, creature)
            runBlocking { ViewInventory().complete(event) }
            assertTrue(GameLogger.getMainHistory().contains("Soldier has:\n\t* Chestplate"))
        }
    }

    @Test
    fun listInventoryEquippedNested() {
        runBlocking {
            val item = createItem("Apple")
            val pouch = Thing("Pouch", equipSlots = listOf(Slot(listOf("Chest"))), properties = Properties(tags = Tags(ITEM)))
            pouch.inventory.add(item)

            val chest = locationRecipe("Chest") { slot("Chest") }

            DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart(chest))
            DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock.fromPart(chest))
            BodyManager.reset()

            val creature = thing("Soldier") {
                body("body")
                props { tag("Container") }
            }.build()
            creature.inventory.add(pouch)
            creature.body.equip(pouch)

            val event = ViewInventoryEvent(GameState.player, creature)
            runBlocking { ViewInventory().complete(event) }
            assertTrue(GameLogger.getMainHistory().contains("Soldier has:\n\t* Pouch\n\t\tApple"))
        }
    }

    @Test
    fun creatureWithoutTagDoesNotListInventory() {
        runBlocking {
            val creature = Thing("Chest")
            creature.inventory.add(Thing("Apple"))
            val event = ViewInventoryEvent(GameState.player, creature)
            runBlocking { ViewInventory().complete(event) }
            assertTrue(GameLogger.getMainHistory().contains("Cannot view inventory of Chest"))
        }
    }


}