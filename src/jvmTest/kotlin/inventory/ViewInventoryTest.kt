package inventory

import core.DependencyInjector
import core.GameState
import core.body.*
import core.history.GameLogger
import core.properties.Properties
import core.properties.TagStrings.CONTAINER
import core.properties.TagStrings.ITEM
import core.properties.Tags
import core.properties.props
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
            creature.add(createItem())
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
            DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart("chest"))
            BodyManager.reset()

            val creature = thing("Soldier") {
                body("body")
                props { tag(CONTAINER) }
            }.build()
            val item = Thing("Chestplate", equipTargets = listOf(EquipTarget("Armor", listOf("chest"))), properties = Properties(tags = Tags(ITEM)))
            creature.add(item)
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
            val pouch = Thing("Pouch", equipTargets = listOf(EquipTarget("Armor", listOf("chest"))), properties = Properties(tags = Tags(ITEM)))
            pouch.add(item)

            DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart("chest"))
            BodyManager.reset()

            val creature = thing("Soldier") {
                body("body")
                props { tag(CONTAINER) }
            }.build()
            creature.add(pouch)
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
            creature.add(Thing("Apple"))
            val event = ViewInventoryEvent(GameState.player, creature)
            runBlocking { ViewInventory().complete(event) }
            assertTrue(GameLogger.getMainHistory().contains("Cannot view inventory of Chest"))
        }
    }


}
