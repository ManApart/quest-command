package traveling.location.weather

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent
import traveling.scope.ScopeManager

class WeatherListener : EventListener<GameTickEvent>() {
    private var lastAppliedEffects = GameState.timeManager.getTicks()
    private val effectAppliedCadence = 5

    override fun execute(event: GameTickEvent) {
        ScopeManager.getScope().changeWeatherIfEnoughTimeHasPassed()
        if (GameState.timeManager.getTicks() > lastAppliedEffects + effectAppliedCadence) {
            lastAppliedEffects = GameState.timeManager.getTicks()
            ScopeManager.getScope().applyWeatherEffects()
        }
    }

}