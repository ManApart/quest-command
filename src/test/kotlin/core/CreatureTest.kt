package core

import core.properties.Properties
import core.properties.Tags
import core.target.Target
import core.properties.Values
import core.target.item.ITEM_TAG
import inventory.createInventoryBody
import status.stat.STRENGTH
import org.junit.Test
import kotlin.test.assertEquals

class CreatureTest {

    @Test
    fun encumbrance0() {
        val creature = Target("creature")
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(createItem(0))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(0, creature.inventory.getWeight())
        assertEquals(0f, creature.getEncumbrance())
    }

    @Test
    fun encumbrance50() {
        val creature = createCreature()
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(createItem(50))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(50, creature.inventory.getWeight())
        assertEquals(.5f, creature.getEncumbrance())
    }

    @Test
    fun encumbrance75() {
        val creature = Target("creature")
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(createItem(75))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(75, creature.inventory.getWeight())
        assertEquals(.75f, creature.getEncumbrance())
    }

    @Test
    fun encumbrance100() {
        val creature = Target("creature")
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(createItem(100))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(100, creature.inventory.getWeight())
        assertEquals(1f, creature.getEncumbrance())
    }

    @Test
    fun encumbranceIsAPercent() {
        val creature = Target("creature")
        creature.soul.addStat(STRENGTH, 100)
        creature.inventory.add(createItem(500))

        assertEquals(1000, creature.getTotalCapacity())
        assertEquals(500, creature.inventory.getWeight())
        assertEquals(.5f, creature.getEncumbrance())
    }

    private fun createCreature(): Target {
        return Target("creature", body = createInventoryBody())
    }

    private fun createItem(weight: Int): Target {
        val properties = Properties(
                Values(mapOf("weight" to weight.toString())),
                Tags(listOf(ITEM_TAG))
        )
        return Target("Target", properties = properties)
    }
}