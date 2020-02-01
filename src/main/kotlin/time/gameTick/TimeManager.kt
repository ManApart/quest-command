package time.gameTick

import core.events.EventListener


class TimeManager(private var ticks: Long = 0) : EventListener<GameTickEvent>() {
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


    override fun execute(event: GameTickEvent) {
        ticks += event.time
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


}