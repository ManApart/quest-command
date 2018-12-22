package gameState

import core.gameState.Item
import core.gameState.Soul
import core.gameState.stat.Stat
import org.junit.Test
import kotlin.test.assertEquals

class StatTest {
    private val statName = "Coding"
    private val parent = Item("Parent")

    @Test
    fun statCurrentIsInitializedProperly() {
        val stat = Stat(statName, 5)
        assertEquals(5, stat.current)
    }

    @Test
    fun incStatCannotBeLowerThan0() {
        val stat = Stat(statName)
        stat.incStat(parent, -10)
        assertEquals(0, stat.current)
    }

    @Test
    fun incMaxStatCannotBeLowerThan0() {
        val stat = Stat(statName)
        stat.incStatMax(parent, -10)
        assertEquals(0, stat.boostedMax)
    }

    @Test
    fun incStatMaxDoesNotIncreaseCurrent() {
        val stat = Stat(statName)
        stat.incStatMax(parent, 5)
        assertEquals(1, stat.current)
    }

    @Test
    fun decreaseStatMaxLowersCurrent() {
        val stat = Stat(statName, 10)
        stat.incStatMax(parent, -7)
        assertEquals(3, stat.current)
    }

    @Test
    fun incStatCannotBeHigherThanMax() {
        val stat = Stat(statName)
        stat.incStatMax(parent, 4)
        stat.incStat(parent, 10)
        assertEquals(5, stat.current)
    }

}