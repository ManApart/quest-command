package traveling.location.weather

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent

class WeatherListener : EventListener<GameTickEvent>() {
    private var lastAppliedEffects = GameState.timeManager.getTicks()
    private val effectAppliedCadence = 5

    override fun execute(event: GameTickEvent) {
        GameState.currentLocation().changeWeatherIfEnoughTimeHasPassed()
        if (GameState.timeManager.getTicks() > lastAppliedEffects + effectAppliedCadence) {
            lastAppliedEffects = GameState.timeManager.getTicks()
            GameState.currentLocation().applyWeatherEffects()
        }
    }

}