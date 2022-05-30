package time

import kotlin.test.Test
import kotlin.test.assertEquals

private data class TestData(val ticks: Int, val expectedDays: Int, val expectedMonths: Int, val expectedYears: Int)

class TimeManagerDateTest {

    private val data = listOf(
        TestData(1, 0, 0, 0),
        TestData(TimeManager.ticksInDay, 1, 0, 0),
        TestData(TimeManager.ticksInDay + 1, 1, 0, 0),
        TestData(TimeManager.ticksInMonth + 1, 0, 1, 0),
        TestData(TimeManager.ticksInYear + 1, 0, 0, 1),
        TestData(TimeManager.ticksInMonth + TimeManager.ticksInDay + 1, 1, 1, 0),
        TestData(TimeManager.ticksInYear + TimeManager.ticksInDay + 1, 1, 0, 1)
    )

    @Test
    fun doTest() {
        data.forEach { run ->
            with(run) {
                val time = TimeManager(ticks)
                assertEquals(expectedYears, time.getYear(), "$ticks should result in $expectedYears years.")
                assertEquals(expectedMonths, time.getMonth(), "$ticks should result in $expectedMonths months.")
                assertEquals(expectedDays, time.getDay(), "$ticks should result in $expectedDays days.")
            }
        }
    }


}