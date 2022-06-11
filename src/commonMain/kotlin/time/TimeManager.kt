package time


class TimeManager(private var ticks: Long = 0) {
    constructor(ticks: Int) : this(ticks.toLong())

    companion object {
        const val ticksInHour = 10
        const val hoursInDay = 10
        const val daysInMonth = 25
        const val monthsInYear = 4

        const val ticksInDay = ticksInHour * hoursInDay
        const val ticksInMonth = ticksInDay * daysInMonth
        const val ticksInYear = ticksInMonth * monthsInYear
    }

    fun passTime(time: Int) {
        this.ticks += time
    }

    fun setTime(ticks: Long) {
        this.ticks = ticks
    }

    fun getTicks(): Long {
        return this.ticks
    }

    fun getYear(): Int {
        return (ticks / ticksInYear).toInt()
    }

    fun getMonth(): Int {
        val remainderFromYear = ticks % ticksInYear
        return (remainderFromYear / (ticksInMonth)).toInt()
    }

    fun getDay(): Int {
        val remainderFromYear = ticks % ticksInYear
        val remainderFromMonth = remainderFromYear % ticksInMonth
        return (remainderFromMonth / ticksInDay).toInt()
    }

    fun getHour(): Int {
        val remainderFromYear = ticks % ticksInYear
        val remainderFromMonth = remainderFromYear % ticksInMonth
        val remainderFromDay = remainderFromMonth % ticksInDay
        return (remainderFromDay / hoursInDay).toInt()
    }

    /**
     * Returns a number between 0 and 100 where 0 is the first tick of the day and 100 is the last tick of the day
     */
    fun getPercentDayComplete(): Int {
        val remainderFromYear = ticks % ticksInYear
        val remainderFromMonth = remainderFromYear % ticksInMonth
        val remainderFromDay = remainderFromMonth % ticksInDay
        return ((remainderFromDay / ticksInDay.toDouble()) * 100).toInt()
    }

    fun getHoursPassed(since: Long = 0): Int {
        val ticksPassed = this.ticks - since
        return (ticksPassed / ticksInHour).toInt()
    }

    fun isNight(): Boolean {
        val percent = getPercentDayComplete()
        return percent < 25 || percent > 75
    }

}