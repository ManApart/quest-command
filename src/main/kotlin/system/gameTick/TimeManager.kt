package system.gameTick

import core.events.EventListener


class TimeManager(private var ticks: Long = 0) : EventListener<GameTickEvent>() {
    constructor(ticks: Int) : this(ticks.toLong())

    companion object {
        const val ticksInDay = 100
        const val daysInMonth = 10
        const val monthsInYear = 10
    }


    override fun execute(event: GameTickEvent) {
        ticks += event.time
    }

    fun getYear(): Int {
        return (ticks / (ticksInDay * daysInMonth * monthsInYear)).toInt()
    }

    fun getMonth(): Int {
        val remainderFromYear = ticks % (ticksInDay * daysInMonth * monthsInYear)
        return (remainderFromYear / (ticksInDay * daysInMonth)).toInt()
    }

    fun getDay(): Int {
        val remainderFromYear = ticks % (ticksInDay * daysInMonth * monthsInYear)
        val remainderFromMonth = remainderFromYear % (ticksInDay * daysInMonth)
        return (remainderFromMonth / (ticksInDay)).toInt()
    }

    /**
     * Returns a number between 0 and 100 where 0 is the first tick of the day and 100 is the last tick of the day
     */
    fun getPercentDayComplete(): Int {
        val remainderFromYear = ticks % (ticksInDay * daysInMonth * monthsInYear)
        val remainderFromMonth = remainderFromYear % (ticksInDay * daysInMonth)
        val remainderFromDay = remainderFromMonth % ticksInDay
        return ((remainderFromDay / ticksInDay.toDouble()) * 100).toInt()
    }


}