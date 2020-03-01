package traveling.location.location

import core.GameState
import core.events.EventListener
import time.gameTick.GameTickEvent
import traveling.scope.Scope
import traveling.scope.ScopeManager

class DayNightListener : EventListener<GameTickEvent>() {

    override fun execute(event: GameTickEvent) {
        val scope = ScopeManager.getScope()
        val percentDayComplete = GameState.timeManager.getPercentDayComplete()
        calculateLight(scope, percentDayComplete)
        calculateHeat(scope, percentDayComplete)
    }

    private fun calculateLight(scope: Scope, percentDayComplete: Int) {
        if (scope.properties.tags.has("Outside")) {
            val previousLightBuff = scope.properties.values.getInt("PreviousLightBuff")
            val light = when{
                percentDayComplete < 20 || percentDayComplete > 80 -> -5
                percentDayComplete == 20 || percentDayComplete == 80 -> -3
                percentDayComplete == 30 || percentDayComplete == 70 -> -2
                else -> 0
            }
            val lightToAdd = light - previousLightBuff
            scope.properties.values.put("PreviousLightBuff", light)
            scope.properties.values.inc(LIGHT, lightToAdd)
        }
    }

    private fun calculateHeat(scope: Scope, percentDayComplete: Int) {
        if (scope.properties.tags.has("Outside")) {
            val previousHeatBuff = scope.properties.values.getInt("PreviousHeatBuff")
            val heat = when{
                percentDayComplete <= 20 || percentDayComplete >= 80 -> -2
                percentDayComplete in 20..23 || percentDayComplete in 76..79 -> -1
                percentDayComplete in 23..26 || percentDayComplete in 73..76 -> 0
                percentDayComplete in 26..29 || percentDayComplete in 70..73 -> 1
                else -> 2
            }
            val heatToAdd = heat - previousHeatBuff
            scope.properties.values.put("PreviousHeatBuff", heat)
            scope.properties.values.inc(HEAT, heatToAdd)
        }
    }

}