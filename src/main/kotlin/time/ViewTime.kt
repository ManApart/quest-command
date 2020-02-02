package time

import core.GameState
import core.events.EventListener
import core.history.display

class ViewTime : EventListener<ViewTimeEvent>() {

    override fun execute(event: ViewTimeEvent) {
        val time = GameState.timeManager
        display("It is hour ${time.getHour()} of day ${time.getDay()} of month ${time.getMonth()} in year ${time.getYear()}. The day is ${time.getPercentDayComplete()}% over.")
    }




}