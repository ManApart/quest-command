package gameState

import org.junit.Test
import status.stat.LeveledStat
import kotlin.test.assertEquals

class LeveledStatTest {
    private val statName = "Coding"

    @Test
    fun statCurrentIsInitializedProperly() {
        val stat = LeveledStat(statName, 5)
        assertEquals(5, stat.current)
    }

    @Test
    fun incStatCannotBeLowerThan0() {
        val stat = LeveledStat(statName)
        stat.incStat(-10)
        assertEquals(0, stat.current)
    }

    @Test
    fun incMaxStatCannotBeLowerThan0() {
        val stat = LeveledStat(statName)
        stat.incStatMax(-10)
        assertEquals(0, stat.max)
    }

    @Test
    fun incStatMaxDoesNotIncreaseCurrent() {
        val stat = LeveledStat(statName)
        stat.incStatMax(5)
        assertEquals(1, stat.current)
    }

    @Test
    fun decreaseStatMaxLowersCurrent() {
        val stat = LeveledStat(statName, 10)
        stat.incStatMax(-7)
        assertEquals(3, stat.current)
    }

    @Test
    fun incStatCannotBeHigherThanMax() {
        val stat = LeveledStat(statName)
        stat.incStatMax(4)
        stat.incStat(10)
        assertEquals(5, stat.current)
    }

}