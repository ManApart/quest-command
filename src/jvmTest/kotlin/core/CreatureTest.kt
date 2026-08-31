package core

import core.properties.Properties
import core.properties.TagStrings.ITEM
import core.properties.Tags
import core.properties.ValueStrings.CAPACITY
import core.properties.Values
import core.thing.Thing
import createMockedGame
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import status.stat.AttributeStrings.STRENGTH
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CreatureTest {
    @BeforeTest
    fun setup() {
        createMockedGame()
    }

    @Test
    fun hasRoom() {
        runBlocking {
            val creature = Thing("creature")
            creature.properties.values.put(CAPACITY, 12)
            creature.add(createItem(0))
            val item2 = createItem(0)

            assertEquals(12, creature.getCapacity())
            assertEquals(1, creature.inventory.getUsedCapacity())
            assertNotNull(creature.getPlaceFor(item2))
        }
    }

    @Test
    fun hasNoRoom() {
        runBlocking {
            val creature = Thing("creature")
            creature.properties.values.put(CAPACITY, 1)
            creature.add(createItem(0))
            val item2 = createItem(0)

            assertEquals(1, creature.getCapacity())
            assertEquals(1, creature.inventory.getUsedCapacity())
            assertNull(creature.getPlaceFor(item2))
        }
    }


    @Test
    fun encumbrance0() {
        runBlocking {
            val creature = Thing("creature")
            creature.soul.addStat(STRENGTH, 10)
            creature.add(createItem(0))

            assertEquals(100, creature.getMaxWeight())
            assertEquals(0, creature.inventory.getWeight())
            assertEquals(0f, creature.getEncumbrance())
        }
    }

    @Test
    fun encumbrance50() {
        runBlocking {
            val creature = createCreature()
            creature.soul.addStat(STRENGTH, 10)
            creature.add(createItem(50))

            assertEquals(100, creature.getMaxWeight())
            assertEquals(50, creature.inventory.getWeight())
            assertEquals(.5f, creature.getEncumbrance())
        }
    }

    @Test
    fun encumbrance75() {
        runBlocking {
            val creature = Thing("creature")
            creature.soul.addStat(STRENGTH, 10)
            creature.add(createItem(75))

            assertEquals(100, creature.getMaxWeight())
            assertEquals(75, creature.inventory.getWeight())
            assertEquals(.75f, creature.getEncumbrance())
        }
    }

    @Test
    fun encumbrance100() {
        runBlocking {
            val creature = Thing("creature")
            creature.soul.addStat(STRENGTH, 10)
            creature.add(createItem(100))

            assertEquals(100, creature.getMaxWeight())
            assertEquals(100, creature.inventory.getWeight())
            assertEquals(1f, creature.getEncumbrance())
        }
    }

    @Test
    fun encumbranceIsAPercent() {
        runBlocking {
            val creature = Thing("creature")
            creature.soul.addStat(STRENGTH, 100)
            creature.add(createItem(500))

            assertEquals(1000, creature.getMaxWeight())
            assertEquals(500, creature.inventory.getWeight())
            assertEquals(.5f, creature.getEncumbrance())
        }
    }

    private fun createCreature(): Thing {
        return Thing("creature")
    }

    private fun createItem(weight: Int): Thing {
        val properties = Properties(
            Values("weight" to weight.toString()),
            Tags(ITEM)
        )
        return Thing("Thing", properties = properties)
    }
}
