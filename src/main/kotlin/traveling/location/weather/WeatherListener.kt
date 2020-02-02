package traveling.location.weather

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent
import traveling.scope.ScopeManager

class WeatherListener : EventListener<GameTickEvent>() {
    private var lastChange = GameState.timeManager.getTicks()
    override fun execute(event: GameTickEvent) {
        val timeNeededToElapse = ScopeManager.getScope().location.weatherChangeFrequency
        if (GameState.timeManager.getHoursPassed(lastChange) >= timeNeededToElapse) {
            lastChange = GameState.timeManager.getTicks()
            ScopeManager.getScope().updateWeather()
        }
    }

}