package core

import core.gameState.Creature
import core.gameState.Item
import core.gameState.stat.STRENGTH
import org.junit.Test
import kotlin.test.assertEquals

class CreatureTest {

    @Test
    fun encumbrance0() {
        val creature = Creature("creature")
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(Item("Item", weight = 0))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(0, creature.inventory.getWeight())
        assertEquals(0f, creature.getEncumbrance())
    }

    @Test
    fun encumbrance50() {
        val creature = Creature("creature")
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(Item("Item", weight = 50))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(50, creature.inventory.getWeight())
        assertEquals(.5f, creature.getEncumbrance())
    }

    @Test
    fun encumbrance75() {
        val creature = Creature("creature")
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(Item("Item", weight = 75))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(75, creature.inventory.getWeight())
        assertEquals(.75f, creature.getEncumbrance())
    }

    @Test
    fun encumbrance100() {
        val creature = Creature("creature")
        creature.soul.addStat(STRENGTH, 10)
        creature.inventory.add(Item("Item", weight = 100))

        assertEquals(100, creature.getTotalCapacity())
        assertEquals(100, creature.inventory.getWeight())
        assertEquals(1f, creature.getEncumbrance())
    }

    @Test
    fun encumbranceIsAPercent() {
        val creature = Creature("creature")
        creature.soul.addStat(STRENGTH, 100)
        creature.inventory.add(Item("Item", weight = 500))

        assertEquals(1000, creature.getTotalCapacity())
        assertEquals(500, creature.inventory.getWeight())
        assertEquals(.5f, creature.getEncumbrance())
    }

}