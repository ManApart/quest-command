package inventory

import core.DependencyInjector
import core.body.*
import core.history.ChatHistory
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.target.item.ITEM_TAG
import core.target.target
import createClosedChest
import createItem
import createMockedGame
import org.junit.Before
import org.junit.Test
import traveling.location.location.locationRecipe
import kotlin.test.assertEquals

class ListInventoryTest {

    @Before
    fun setup() {
        createMockedGame()
    }

    @Test
    fun listInventory() {
        val creature = createClosedChest()
        creature.inventory.add(createItem())
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Closed Chest has:\n\tApple", ChatHistory.getLastOutput())
    }

    @Test
    fun listNoItemsInventory() {
        val creature = createClosedChest()
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Closed Chest has no items.", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquipped() {
        val chest = locationRecipe("chest") { slot("chest") }

        DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart(chest))
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock.fromPart(chest))
        BodyManager.reset()

        val creature = target("Soldier") {
            body("body")
            props { tag("Container")}
        }.build()
        val item = Target("Chestplate", equipSlots = listOf(Slot(listOf("Chest"))), properties = Properties(tags = Tags(ITEM_TAG)))
        creature.inventory.add(item)
        creature.body.equip(item)
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Chestplate", ChatHistory.getLastOutput())
    }

    @Test
    fun listInventoryEquippedNested() {
        val item = createItem("Apple")
        val pouch = Target("Pouch", equipSlots = listOf(Slot(listOf("Chest"))), properties = Properties(tags = Tags(ITEM_TAG)))
        pouch.inventory.add(item)

        val chest = locationRecipe("Chest") { slot("Chest") }

        DependencyInjector.setImplementation(BodysCollection::class, BodysMock.fromPart(chest))
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock.fromPart(chest))
        BodyManager.reset()

        val creature = target("Soldier") {
            body("body")
            props { tag("Container")}
        }.build()
        creature.inventory.add(pouch)
        creature.body.equip(pouch)

        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Soldier has:\n\t* Pouch\n\t\tApple", ChatHistory.getLastOutput())
    }

    @Test
    fun creatureWithoutTagDoesNotListInventory() {
        val creature = Target("Chest")
        creature.inventory.add(Target("Apple"))
        val event = ListInventoryEvent(creature)
        ListInventory().execute(event)
        assertEquals("Cannot view inventory of Chest", ChatHistory.getLastOutput())
    }


}