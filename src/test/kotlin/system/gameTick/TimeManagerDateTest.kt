package system.gameTick

import time.gameTick.TimeManager
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class TimeManagerDateTest(private val ticks: Int, private val expectedDays: Int, private val expectedMonths: Int, private val expectedYears: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Int>> {
            return listOf(
                    arrayOf(1, 0, 0, 0),
                    arrayOf(TimeManager.ticksInDay, 1, 0, 0),
                    arrayOf(TimeManager.ticksInDay + 1, 1, 0, 0),
                    arrayOf(TimeManager.ticksInDay * TimeManager.daysInMonth + 1, 0, 1, 0),
                    arrayOf(TimeManager.ticksInDay * TimeManager.daysInMonth * TimeManager.monthsInYear + 1, 0, 0, 1),
                    arrayOf(TimeManager.ticksInDay * TimeManager.daysInMonth + TimeManager.ticksInDay + 1, 1, 1, 0),
                    arrayOf(TimeManager.ticksInDay * TimeManager.daysInMonth * TimeManager.monthsInYear + TimeManager.ticksInDay + 1, 1, 0, 1)
            )
        }
    }

    @Test
    fun doTest() {
        val time = TimeManager(ticks)
        assertEquals(expectedYears, time.getYear(), "$ticks should result in $expectedYears years.")
        assertEquals(expectedMonths, time.getMonth(), "$ticks should result in $expectedMonths months.")
        assertEquals(expectedDays, time.getDay(), "$ticks should result in $expectedDays days.")
    }


}