package traveling.scope

import core.GameState
import core.target.Target
import traveling.location.location.HEAT


fun getHeatLevel(scope: Scope): Int {
    return scope.location.properties.values.getInt(HEAT) +
            scope.weather.properties.values.getInt(HEAT) +
            getDayBonus(scope)
}

private fun getDayBonus(scope: Scope): Int {
    val percentDayComplete = GameState.timeManager.getPercentDayComplete()
    return when {
        !scope.location.properties.tags.has("Outside") -> 0
        percentDayComplete <= 20 || percentDayComplete >= 80 -> -2
        percentDayComplete in 20..23 || percentDayComplete in 76..79 -> -1
        percentDayComplete in 23..26 || percentDayComplete in 73..76 -> 0
        percentDayComplete in 26..29 || percentDayComplete in 70..73 -> 1
        else -> 2
    }
}


fun getHeatLevel(scope: Scope, target: Target): Int {
    return getHeatLevel(scope) + target.properties.values.getInt(HEAT)
}