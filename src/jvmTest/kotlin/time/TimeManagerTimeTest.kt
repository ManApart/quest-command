package time

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TimeManagerTimeTest {

    @Test
    fun getPercentDayComplete0() {
        val time = TimeManager()
        assertEquals(0, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayComplete25() {
        val time = TimeManager(TimeManager.ticksInDay / 4)
        assertEquals(25, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayComplete50() {
        val time = TimeManager(TimeManager.ticksInDay / 2)
        assertEquals(50, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayComplete99() {
        val time = TimeManager(TimeManager.ticksInDay - 1)
        assertEquals(99, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayComplete100() {
        val time = TimeManager(TimeManager.ticksInDay)
        assertEquals(0, time.getPercentDayComplete())
    }

    @Test
    fun getPercentDayCompleteDay2() {
        val time = TimeManager(TimeManager.ticksInDay + 1)
        assertEquals(1, time.getPercentDayComplete())
    }

    @Test
    fun isNight() {
        val time = TimeManager()
        assertTrue(time.isNight())

        val ticksAtEndOfMorning = (TimeManager.ticksInHour * TimeManager.hoursInDay / 4).toLong()
        time.setTime(ticksAtEndOfMorning - 1)
        assertTrue(time.isNight())

        time.setTime(ticksAtEndOfMorning + 1)
        assertFalse(time.isNight())

        val ticksAtStartOfNight = (TimeManager.ticksInHour * TimeManager.hoursInDay * 3 / 4).toLong()
        time.setTime(ticksAtStartOfNight - 1)
        assertFalse(time.isNight())

        time.setTime(ticksAtStartOfNight + 1)
        assertTrue(time.isNight())
    }

    @Test
    fun getDateAndTime() {
        val year = 2 * TimeManager.ticksInYear
        val month = 3 * TimeManager.ticksInMonth
        val day = 5 * TimeManager.ticksInDay
        val hour = 2 * TimeManager.ticksInHour

        val time = TimeManager(year + month + day + hour)

        assertEquals(2, time.getYear())
        assertEquals(3, time.getMonth())
        assertEquals(5, time.getDay())
        assertEquals(2, time.getHour())
    }

    @Test
    fun getHourPassedInSameDay() {
        val time = TimeManager()
        val initial = time.getTicks()
        time.passTime(2 * TimeManager.ticksInHour)

        assertEquals(2, time.getHoursPassed(initial))
    }

    @Test
    fun getHoursPassedOverADay() {
        val time = TimeManager()
        val initial = time.getTicks()
        time.passTime(TimeManager.ticksInDay)

        assertEquals(TimeManager.hoursInDay, time.getHoursPassed(initial))
    }


}