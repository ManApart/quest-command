package gameState

import core.gameState.Target
import core.gameState.Soul
import core.gameState.stat.Stat
import org.junit.Test
import kotlin.test.assertEquals

class StatTest {
    private val statName = "Coding"
    private val parent = Target("Parent")

    @Test
    fun statCurrentIsInitializedProperly() {
        val stat = Stat(statName, parent, 5)
        assertEquals(5, stat.current)
    }

    @Test
    fun incStatCannotBeLowerThan0() {
        val stat = Stat(statName, parent)
        stat.incStat(-10)
        assertEquals(0, stat.current)
    }

    @Test
    fun incMaxStatCannotBeLowerThan0() {
        val stat = Stat(statName, parent)
        stat.incStatMax(-10)
        assertEquals(0, stat.max)
    }

    @Test
    fun incStatMaxDoesNotIncreaseCurrent() {
        val stat = Stat(statName, parent)
        stat.incStatMax(5)
        assertEquals(1, stat.current)
    }

    @Test
    fun decreaseStatMaxLowersCurrent() {
        val stat = Stat(statName, parent, 10)
        stat.incStatMax(-7)
        assertEquals(3, stat.current)
    }

    @Test
    fun incStatCannotBeHigherThanMax() {
        val stat = Stat(statName, parent)
        stat.incStatMax(4)
        stat.incStat(10)
        assertEquals(5, stat.current)
    }

}