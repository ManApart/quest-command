package traveling.location.weather

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent
import traveling.scope.ScopeManager

class WeatherListener : EventListener<GameTickEvent>() {
    var lastHour = GameState.timeManager.getHour()
    override fun execute(event: GameTickEvent) {
        val hour = GameState.timeManager.getHour()
        if (hour != lastHour) {
            lastHour = hour
            ScopeManager.getScope().updateWeather()
        }
    }

}