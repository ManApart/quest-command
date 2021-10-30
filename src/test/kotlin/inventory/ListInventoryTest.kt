package inventory

import core.DependencyInjector
import core.GameState
import core.body.*
import core.history.GameLogger
import core.properties.Properties
import core.properties.Tags
import core.thing.Thing
import core.thing.item.ITEM_TAG
import core.thing.thing
import createClosedChest
import createItem
import createMockedGame
import org.junit.Before
import org.junit.Test
import system.debug.DebugType
import traveling.location.location.locationRecipe
import kotlin.test.assertEquals

class ListInventoryTest {

    @Before
    fun setup() {
        createMockedGame()
        GameState.putDebug(DebugType.CLARITY, true)
    }

    @Test
    fun listInventory() {
        val creature = createClosedChest()
        creature.inventory.add(createItem())
        val event = ListInventoryEvent(GameState.player, creature)
        ListInventory().execute(event)
        assertEquals("Closed Chest has:\n\tApple", GameLogger.main.getLastOutput())
    }

    @Test
    fun listNoItemsInventory() {
        val creature = createClosedChest()
        val event = ListInventoryEvent(GameState.player, creature)
        ListInventory().execute(event)
        assertEquals("Closed Chest has no items.", GameLogger.main.getLastOutput())
    }

    @Test
    fun listInventoryEquipped() {
        val chest = locationRecipe("chest") { slot("chest") }

        DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart(chest))
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock.fromPart(chest))
        BodyManager.reset()

        val creature = thing("Soldier") {
            body("body")
            props { tag("Container")}
        }.build()
        val item = Thing("Chestplate", equipSlots = listOf(Slot(listOf("Chest"))), properties = Properties(tags = Tags(ITEM_TAG)))
        creature.inventory.add(item)
        creature.body.equip(item)
        val event = ListInventoryEvent(GameState.player, creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Chestplate", GameLogger.main.getLastOutput())
    }

    @Test
    fun listInventoryEquippedNested() {
        val item = createItem("Apple")
        val pouch = Thing("Pouch", equipSlots = listOf(Slot(listOf("Chest"))), properties = Properties(tags = Tags(ITEM_TAG)))
        pouch.inventory.add(item)

        val chest = locationRecipe("Chest") { slot("Chest") }

        DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart(chest))
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock.fromPart(chest))
        BodyManager.reset()

        val creature = thing("Soldier") {
            body("body")
            props { tag("Container")}
        }.build()
        creature.inventory.add(pouch)
        creature.body.equip(pouch)

        val event = ListInventoryEvent(GameState.player, creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Pouch\n\t\tApple", GameLogger.main.getLastOutput())
    }

    @Test
    fun creatureWithoutTagDoesNotListInventory() {
        val creature = Thing("Chest")
        creature.inventory.add(Thing("Apple"))
        val event = ListInventoryEvent(GameState.player, creature)
        ListInventory().execute(event)
        assertEquals("Cannot view inventory of Chest", GameLogger.main.getLastOutput())
    }


}